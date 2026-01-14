package org.legstar.cobol.copybook.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.legstar.cobol.copybook.parser.CopybookCleaner.Result;
import org.legstar.cobol.data.entry.CobolDataEntry;

import org.legstar.cobol.copybook.ccparser.CopybookCCParser;
import org.legstar.cobol.copybook.ccparser.Node;
import org.legstar.cobol.copybook.ccparser.ParseException;

/**
 * From a COBOL copybook source produces a set of Cobol data description
 * entries.
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
		CopybookCCParser ccParser;
		try {
			ccParser = new CopybookCCParser(inputSource, cleanResult.toCobolSource());
			ccParser.Copybook();
			return produce(ccParser.rootNode(), cleanResult::originalLineNumber);
		} catch (ParseException e) {
			throw new CopybookParserException(e, cleanResult::originalLineNumber);
		}
	}

	List<CobolDataEntry> produce(Node node, Function<Integer, Integer> lineNumberProvider) {
		List<CobolDataEntry> cobolDataEntries = new ArrayList<>();
		CopybookParserVisitor visitor = new CopybookParserVisitor(cobolDataEntries, lineNumberProvider);
		visitor.visit(node);
		return cobolDataEntries;
	}

}
