package com.legsem.legstar.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.legsem.legstar.copybook.CobolDataEntry;
import com.legsem.legstar.parser.CopybookCleaner.Result;
import com.legsem.legstar.ccparser.CopybookCCParser;
import com.legsem.legstar.ccparser.Node;
import com.legsem.legstar.ccparser.ParseException;

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
		Function<Integer, Integer> lineNumberProvider = cleanResult::originalLineNumber;
		CopybookCCParser ccParser;
		try {
			ccParser = new CopybookCCParser(inputSource, cleanResult.toCobolSource());
			ccParser.Copybook();
			return produce(ccParser.rootNode(), lineNumberProvider);
		} catch (ParseException e) {
			throw new CopybookParserException(e, lineNumberProvider);
		}
	}

	List<CobolDataEntry> produce(Node node, Function<Integer, Integer> lineNumberProvider) {
		List<CobolDataEntry> cobolDataEntries = new ArrayList<>();
		CopybookParserVisitor visitor = new CopybookParserVisitor(cobolDataEntries, lineNumberProvider);
		visitor.visit(node);
		return cobolDataEntries;
	}

}
