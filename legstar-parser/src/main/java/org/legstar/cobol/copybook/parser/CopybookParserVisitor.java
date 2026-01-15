package org.legstar.cobol.copybook.parser;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.data.entry.CobolDataEntryUsage;
import org.legstar.cobol.data.entry.CobolDataEntry.Range;

import org.legstar.cobol.copybook.ccparser.Node.Visitor;
import org.legstar.cobol.copybook.ccparser.ast.ConditionValue;
import org.legstar.cobol.copybook.ccparser.ast.Copybook;
import org.legstar.cobol.copybook.ccparser.ast.DATA_NAME;
import org.legstar.cobol.copybook.ccparser.ast.DATE_PATTERN;
import org.legstar.cobol.copybook.ccparser.ast.DataEntryFormat1;
import org.legstar.cobol.copybook.ccparser.ast.DataEntryFormat3;
import org.legstar.cobol.copybook.ccparser.ast.DateFormatClause;
import org.legstar.cobol.copybook.ccparser.ast.FixedLengthOccurs;
import org.legstar.cobol.copybook.ccparser.ast.INTEGER;
import org.legstar.cobol.copybook.ccparser.ast.LEADING;
import org.legstar.cobol.copybook.ccparser.ast.LEVEL_77;
import org.legstar.cobol.copybook.ccparser.ast.LEVEL_88;
import org.legstar.cobol.copybook.ccparser.ast.LEVEL_NUMBER;
import org.legstar.cobol.copybook.ccparser.ast.Literal;
import org.legstar.cobol.copybook.ccparser.ast.LiteralRange;
import org.legstar.cobol.copybook.ccparser.ast.LowerBound;
import org.legstar.cobol.copybook.ccparser.ast.PictureString;
import org.legstar.cobol.copybook.ccparser.ast.RedefinesClause;
import org.legstar.cobol.copybook.ccparser.ast.SignClause;
import org.legstar.cobol.copybook.ccparser.ast.SignSeparate;
import org.legstar.cobol.copybook.ccparser.ast.UpperBound;
import org.legstar.cobol.copybook.ccparser.ast.UsageValue;
import org.legstar.cobol.copybook.ccparser.ast.ValueClause;
import org.legstar.cobol.copybook.ccparser.ast.VariableLengthOccurs;

/**
 * This visitor organizes nodes from the CC AST into a hierarchy based on the
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
	private final Function<Integer, Integer> lineNumberProvider;

	/**
	 * Pending data entries not yet completed.
	 */
	private final Stack<CobolDataEntry.Builder> pendingEntries = new Stack<>();

	/**
	 * @param cobolDataEntries   the top level data entries to be populated
	 * @param lineNumberProvider a function to retrieve the line position in the
	 *                           original source code before cleanup.
	 */
	public CopybookParserVisitor(List<CobolDataEntry> cobolDataEntries, Function<Integer, Integer> lineNumberProvider) {
		this.cobolDataEntries = cobolDataEntries;
		this.lineNumberProvider = lineNumberProvider;
	}

	/**
	 * Visit all nodes in the copybook and then process an pending entries left.
	 */
	public void visit(Copybook node) {
		recurse(node);
		while (!pendingEntries.isEmpty()) {
			addCobolDataEntry(pendingEntries.pop().build());
		}
	}

	/**
	 * A regular Data Description Entry (not a Rename or Condition).
	 * <p>
	 * Get the level number and pop all entries in the stack that are not ancestors
	 * of this new entry.
	 * <p>
	 * Adjust the line number so that it reflects the line position in the original
	 * source code before cleanup.
	 */
	public void visit(DataEntryFormat1 node) {

		LEVEL_NUMBER levelNumberNode = node.firstChildOfType(LEVEL_NUMBER.class);
		LEVEL_77 level77Node = node.firstChildOfType(LEVEL_77.class);
		int levelNumber = level77Node == null //
				? Integer.parseInt(levelNumberNode.toString()) //
				: Integer.parseInt(level77Node.toString());

		popNonAncestors(levelNumber);

		CobolDataEntry.Builder builder = new CobolDataEntry.Builder() //
				.srceLine(lineNumberProvider.apply(node.getBeginLine())) //
				.levelNumber(levelNumber);

		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);
		if (dataNameNode != null) {
			builder.cobolName(dataNameNode.toString());
		}

		pendingEntries.push(builder);
		recurse(node);
	}

	/**
	 * Any pending entry that is not an ancestor of the new entry must be finalized
	 * and popped from the pending entries list.
	 */
	private void popNonAncestors(int levelNumber) {
		while (!pendingEntries.isEmpty()) {
			if (pendingEntries.peek().isHigherThan(levelNumber)) {
				break;
			} else {
				addCobolDataEntry(pendingEntries.pop().build());
			}
		}
	}

	/**
	 * Add a new entry either as a top level entry or as a child of a parent entry
	 * (on top of the stack).
	 */
	private void addCobolDataEntry(CobolDataEntry entry) {
		if (pendingEntries.isEmpty()) {
			cobolDataEntries.add(entry);
		} else {
			CobolDataEntry.Builder parentBuilder = pendingEntries.peek();
			parentBuilder.addChild(entry);
		}
	}

	public void visit(PictureString node) {
		pendingEntries.peek().picture(node.toString());
		recurse(node);
	}

	public void visit(UsageValue node) {
		pendingEntries.peek().usage(CobolDataEntryUsage.fromCobolString(node.toString()));
		recurse(node);
	}

	public void visit(ValueClause node) {
		Literal literalNode = node.firstChildOfType(Literal.class);
		pendingEntries.peek().value(literalNode.toString());
		recurse(node);
	}

	public void visit(SignClause node) {
		LEADING leadingNode = node.firstChildOfType(LEADING.class);
		pendingEntries.peek().signLeading(leadingNode != null);
		recurse(node);
	}

	public void visit(SignSeparate node) {
		pendingEntries.peek().signSeparate(true);
		recurse(node);
	}

	public void visit(FixedLengthOccurs node) {
		INTEGER maxOccursNode = node.firstChildOfType(INTEGER.class);
		int maxOccurs = Integer.parseInt(maxOccursNode.toString());
		pendingEntries.peek() //
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

		pendingEntries.peek() //
				.maxOccurs(maxOccurs) //
				.minOccurs(minOccurs) //
				.dependingOn(dataNameNode.toString());
		recurse(node);
	}

	public void visit(RedefinesClause node) {
		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);
		pendingEntries.peek().redefines(dataNameNode.toString());
		recurse(node);
	}

	public void visit(DateFormatClause node) {
		DATE_PATTERN dataPatternNode = node.firstChildOfType(DATE_PATTERN.class);
		pendingEntries.peek().dateFormat(dataPatternNode.toString());
		recurse(node);
	}

	/**
	 * A Condition Data entry.
	 * <p>
	 * These are level 88 entries and a Data name is mandatory.
	 */
	public void visit(DataEntryFormat3 node) {

		LEVEL_88 level88Node = node.firstChildOfType(LEVEL_88.class);
		int levelNumber = Integer.parseInt(level88Node.toString());

		popNonAncestors(levelNumber);

		DATA_NAME dataNameNode = node.firstChildOfType(DATA_NAME.class);
		CobolDataEntry.Builder builder = new CobolDataEntry.Builder() //
				.srceLine(lineNumberProvider.apply(node.getBeginLine())) //
				.levelNumber(levelNumber) //
				.cobolName(dataNameNode.toString());

		List<ConditionValue> conditionValues = node.childrenOfType(ConditionValue.class);
		conditionValues.forEach(cv -> {
			LiteralRange literalRange = cv.firstChildOfType(LiteralRange.class);
			if (literalRange == null) {
				Literal literal = cv.firstChildOfType(Literal.class);
				builder.addConditionLiteral(literal.toString());
			} else {
				List<Literal> literals = literalRange.childrenOfType(Literal.class);
				String from = literals.get(0).toString();
				String to = literals.get(1).toString();
				builder.addConditionRange(new Range(from, to));
			}
		});

		pendingEntries.push(builder);
		recurse(node);
	}
}
