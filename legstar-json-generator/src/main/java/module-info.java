/**
 * Generates COBOL and JSON annotated JAVA beans from COBOL copybooks
 */
module org.legstar.cobol.jaxb.generator {
	
	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.generator;

	exports org.legstar.cobol.json.generator;
	
}