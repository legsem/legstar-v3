/**
 * Generates COBOL and XML annotated JAVA beans from COBOL copybooks
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