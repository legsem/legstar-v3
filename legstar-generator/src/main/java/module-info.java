/**
 * Generates COBOL annotated JAVA beans from COBOL copybooks
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