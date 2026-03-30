
/**
 * Generates Cobol annotated Java beans from Cobol copybooks.
 * <p>
 * The {@link org.legstar.cobol.bean.generator.CobolBeanGenerator CobolBeanGenerator}
 * can be used like so:
 * 
 * <pre>
 * Path path = Paths.get("src/cobol/CUSTDAT.cpy"); // Path to copybook file
 * Reader reader = Files.newBufferedReader(path);
 * StringWriter writer = new StringWriter(); // Generated bean source
 * new CobolBeanGenerator().generate("custdat", reader, writer); // "custdat" is package last segment
 * </pre>
 * 
 * Internally, this calls the legstar-parser, then produces a
 * {@link org.legstar.cobol.generator.model.RenderingModel RenderingModel}.
 * <p>
 * Finally the JTE templating engine is used to produce the Java class from the
 * RenderingModel.
 */

module org.legstar.cobol.generator {

	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.parser;
	requires transitive gg.jte.runtime;

	exports org.legstar.cobol.generator;
	exports org.legstar.cobol.bean.generator;
	exports org.legstar.cobol.generator.model;
	exports org.legstar.cobol.bean.generator.templates;

}