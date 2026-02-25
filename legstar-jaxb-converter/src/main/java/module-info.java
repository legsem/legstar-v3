/**
 * Converters for COBOL data items to XML using JAXB 
 */
module org.legstar.cobol.jaxb.converter {
	
	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.converter;
	requires transitive jakarta.xml.bind;
	requires transitive org.glassfish.jaxb.runtime;
	
	exports org.legstar.cobol.jaxb.converter;
	
}