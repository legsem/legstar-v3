/**
 * Generates Cobol and JAXB annotated Java beans from Cobol copybooks.
 * <p>
 * The {@link org.legstar.cobol.jaxb.generator.CobolJaxbGenerator
 * CobolJaxbGenerator} can be used like so:
 * 
 * <pre>
 * Path path = Paths.get("src/cobol/CUSTDAT.cpy"); // Path to copybook file
 * Reader reader = Files.newBufferedReader(path);
 * StringWriter writer = new StringWriter(); // Generated bean source
 * new CobolJaxbGenerator().generate("custdat", reader, writer); // "custdat" is package last segment
 * </pre>
 * 
 * This is an extension to legstar-generator that injects additional JAXB
 * annotations into the generated bean class.
 * 
 */
module org.legstar.cobol.jaxb.generator {

	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.generator;

	requires transitive jakarta.xml.bind;
	requires transitive org.glassfish.jaxb.runtime;
	requires gg.jte.runtime;

	exports org.legstar.cobol.jaxb.generator;
	exports org.legstar.cobol.jaxb.generator.templates;

}