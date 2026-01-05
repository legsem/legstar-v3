package org.legsem.congocc.test;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.legsem.cobol.copybook.model.CobolDataEntry;
import org.legsem.congocc.test.CopybookCleaner.Result;
import org.legstar.copybook.parser.CopybookCCParser;
import org.legstar.copybook.parser.Node;

/**
 * From a COBOL copybook source produces a set of Cobol data description entries
 * (CobolDataItem).
 */
public class CopybookParser {

	private CopybookConfig config;

	public CopybookParser() {
		this(new CopybookConfig());
	}

	public CopybookParser(CopybookConfig config) {
		this.config = config;
	}

	public List<CobolDataEntry> parse(String inputSource, Reader reader) {
		CopybookCleaner cleaner = new CopybookCleaner(this.config);
		Result cleanResult = cleaner.clean(reader);
		// TODO capture CC parsing exceptions and translate line numbers into real line
		// numbers
		CopybookCCParser ccParser = new CopybookCCParser(inputSource, cleanResult.toCobolSource());
		ccParser.Copybook();
		return produce(ccParser.rootNode(), ln -> cleanResult.realLineNumber(ln));
	}

	List<CobolDataEntry> produce(Node node, Function<Integer, Integer> realLineNumber) {
		List<CobolDataEntry> cobolDataEntries = new ArrayList<>();
		CopybookParserVisitor visitor = new CopybookParserVisitor(cobolDataEntries, realLineNumber);
		visitor.visit(node);
		return cobolDataEntries;
	}

}
