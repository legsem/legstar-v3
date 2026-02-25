package org.legstar.cobol.copybook.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.legstar.cobol.copybook.parser.CopybookParserCleaner.Result;
import org.legstar.cobol.data.entry.CobolDataEntry;

import org.legstar.cobol.copybook.ccparser.CopybookCCParser;
import org.legstar.cobol.copybook.ccparser.CopybookParserVisitor;
import org.legstar.cobol.copybook.ccparser.Node;
import org.legstar.cobol.copybook.ccparser.ParseException;

/**
 * From a COBOL copybook source produces a set of Cobol data description
 * entries.
 */
public class CopybookParser {

	private CopybookParserConfig config;

	/**
	 * Create a parser with default configuration.
	 */
	public CopybookParser() {
		this(new CopybookParserConfig());
	}

	/**
	 * Create a parser
	 * 
	 * @param config configuration parameters
	 */
	public CopybookParser(CopybookParserConfig config) {
		this.config = config;
	}

	/**
	 * Parses a copybook into one or more CobolDataEntry. A CobolDataEntry is the
	 * Abstract Syntax Tree.
	 * 
	 * @param inputSource a name for the input source for reporting purposes (file
	 *                    name for instance)
	 * @param reader      reads the cobol copybook
	 * @return a list of CobolDataEntry (there could be more than one if the
	 *         copybook contains multiple root items)
	 */
	public List<CobolDataEntry> parse(String inputSource, Reader reader) {
		CopybookParserCleaner cleaner = new CopybookParserCleaner(this.config);
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

	/**
	 * Produce the list of CobolDataEntry (Abstract Syntax Tree) out of the nodes
	 * returned by CongoCC.
	 * 
	 * @param node               the CongoCC main node
	 * @param lineNumberProvider a function to reassign line numbers. This is needed
	 *                           because we pass a subset of the original lines to
	 *                           CongoCC but need errors to report the original line
	 *                           number.
	 * @return a list of CobolDataEntry
	 */
	List<CobolDataEntry> produce(Node node, Function<Integer, Integer> lineNumberProvider) {
		List<CobolDataEntry> cobolDataEntries = new ArrayList<>();
		CopybookParserVisitor visitor = new CopybookParserVisitor(cobolDataEntries, lineNumberProvider);
		visitor.visit(node);
		return cobolDataEntries;
	}

}
