/**
 * Generates Cobol and JACKSON annotated Java beans from Cobol copybooks.
 * <p>
 * The {@link org.legstar.cobol.json.generator.CobolJsonGenerator
 * CobolJsonGenerator} can be used like so:
 * 
 * <pre>
 * Path path = Paths.get("src/cobol/CUSTDAT.cpy"); // Path to copybook file
 * Reader reader = Files.newBufferedReader(path);
 * StringWriter writer = new StringWriter(); // Generated bean source
 * new CobolJsonGenerator().generate("custdat", reader, writer); // "custdat" is package last segment
 * </pre>
 * 
 * This is an extension to legstar-generator that injects additional JACKSON
 * annotations into the generated bean class.
 * 
 */
module org.legstar.cobol.json.generator {

	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.generator;

	exports org.legstar.cobol.json.generator;

}