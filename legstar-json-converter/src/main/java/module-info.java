/**
 * Converters for COBOL data items to JSON 
 */
module org.legstar.cobol.json.converter {
	
	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.converter;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive com.fasterxml.jackson.core;
	
	exports org.legstar.cobol.json.converter;
	
}