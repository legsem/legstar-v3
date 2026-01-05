package org.legsem.congocc.test;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import org.legsem.cobol.copybook.model.CobolDataEntry;
import org.legsem.cobol.copybook.model.CobolUsage;
import org.legstar.copybook.parser.Node.Visitor;
import org.legstar.copybook.parser.ast.Copybook;
import org.legstar.copybook.parser.ast.DATA_NAME;
import org.legstar.copybook.parser.ast.DataEntryFormat1;
import org.legstar.copybook.parser.ast.FixedLengthOccurs;
import org.legstar.copybook.parser.ast.INTEGER;
import org.legstar.copybook.parser.ast.LEVEL_77;
import org.legstar.copybook.parser.ast.LEVEL_NUMBER;
import org.legstar.copybook.parser.ast.Literal;
import org.legstar.copybook.parser.ast.LowerBound;
import org.legstar.copybook.parser.ast.PictureString;
import org.legstar.copybook.parser.ast.RedefinesClause;
import org.legstar.copybook.parser.ast.UpperBound;
import org.legstar.copybook.parser.ast.UsageClause;
import org.legstar.copybook.parser.ast.UsageValue;
import org.legstar.copybook.parser.ast.ValueClause;
import org.legstar.copybook.parser.ast.VariableLengthOccurs;

/**
 * The visitor organizes nodes from the CC AST into a hierarchy based on the
 * cobol level numbers and produces a set of data entries.
 */
public class CopybookParserVisitor extends Visitor {

	/**
	 * Ordered list of high level data entries in the copybook.
	 */
	private final List<CobolDataEntry> cobolDataEntries;

	/**
	 * A function to restore a real line number for each data entry (Preceding
	 * cleanup process may have deleted lines before CC parsing)..
	 */
	private final Function<Integer, Integer> realLineNumber;

	/**
	 * Data entries hierarchy.
	 */
	private final Stack<CobolDataEntry.Builder> builders = new Stack<>();

	public CopybookParserVisitor(List<CobolDataEntry> cobolDataEntries, Function<Integer, Integer> realLineNumber) {
		this.cobolDataEntries = cobolDataEntries;
		this.realLineNumber = realLineNumber;
	}

	public void visit(Copybook node) {
		recurse(node);
		while (!builders.isEmpty()) {
			addCobolDataEntry(builders.pop().build());
		}
	}

	public void visit(DataEntryFormat1 node) {

		LEVEL_NUMBER levelNumberNode = node.firstChildOfType(LEVEL_NUMBER.class);
		LEVEL_77 level77Node = node.firstChildOfType(LEVEL_77.class);
		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);
		int levelNumber = level77Node == null //
				? Integer.parseInt(levelNumberNode.toString()) //
				: Integer.parseInt(level77Node.toString());

		while (!builders.isEmpty()) {
			if (builders.peek().isHigherThan(levelNumber)) {
				break;
			} else {
				addCobolDataEntry(builders.pop().build());
			}
		}

		int lineNumber = realLineNumber.apply(node.getBeginLine());
		CobolDataEntry.Builder builder = new CobolDataEntry.Builder() //
				.srceLine(lineNumber) // real line number
				.levelNumber(levelNumber) //
				.cobolName(dataNameNode.toString());
		builders.push(builder);
		recurse(node);
	}

	private void addCobolDataEntry(CobolDataEntry entry) {
		if (builders.isEmpty()) {
			cobolDataEntries.add(entry);
		} else {
			CobolDataEntry.Builder parentBuilder = builders.peek();
			parentBuilder.addChild(entry);
		}
	}

	public void visit(PictureString node) {
		builders.peek().picture(node.toString());
	}

	public void visit(UsageValue node) {
		builders.peek().usage(CobolUsage.fromCobolString(node.toString()));
	}

	public void visit(ValueClause node) {
		Literal literalNode = node.firstChildOfType(Literal.class);
		builders.peek().value(literalNode.toString());
	}

	public void visit(FixedLengthOccurs node) {
		INTEGER maxOccursNode = node.firstChildOfType(INTEGER.class);
		int maxOccurs = Integer.parseInt(maxOccursNode.toString());
		builders.peek() //
				.maxOccurs(maxOccurs) //
				.minOccurs(maxOccurs);
		recurse(node);
	}

	public void visit(VariableLengthOccurs node) {

		int minOccurs = 0;
		LowerBound lowerBoundNode = node.firstChildOfType(LowerBound.class);
		if (lowerBoundNode != null) {
			INTEGER minOccursNode = lowerBoundNode.firstChildOfType(INTEGER.class);
			minOccurs = Integer.parseInt(minOccursNode.toString());
		}

		UpperBound upperBoundNode = node.firstChildOfType(UpperBound.class);
		INTEGER maxOccursNode = upperBoundNode.firstChildOfType(INTEGER.class);
		int maxOccurs = Integer.parseInt(maxOccursNode.toString());

		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);

		builders.peek() //
				.maxOccurs(maxOccurs) //
				.minOccurs(minOccurs) //
				.dependingOn(dataNameNode.toString());
		recurse(node);
	}

	public void visit(RedefinesClause node) {
		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);
		builders.peek().redefines(dataNameNode.toString());
	}

}
