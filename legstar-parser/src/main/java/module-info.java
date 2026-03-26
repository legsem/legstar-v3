/**
 * Parses a COBOL copybook into an abstract syntax tree.
 * <p>
 * In order to call the parser, you can do:
 * 
 * <pre>
 * CopybookParser parser = new CopybookParser(); // Pass a config if you need more options
 * Path path = Paths.get("src/cobol/CUSTDAT.cpy"); // Path to copybook file
 * Reader reader = Files.newBufferedReader(path);
 * List&lt;CobolDataEntry&gt; entries = parser.parse("custdat", reader); // first parameter is an identifier
 * </pre>
 * 
 * The output List&lt;CobolDataEntry&gt; usually contains a single
 * {@link org.legstar.cobol.data.entry.CobolDataEntry CobolDataEntry} (Abstract
 * Syntax Tree) unless the copybook contains several root data entries (level
 * 01).
 */
module org.legstar.cobol.parser {

	exports org.legstar.cobol.copybook.parser;
	exports org.legstar.cobol.data.entry;

}