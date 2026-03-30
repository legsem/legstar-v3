/**
 * Converters for COBOL data entries to JSON.
 * <p>
 * The prerequisite is to use the legstar-json-generator to produce a cobol and
 * JACKSON annotated java bean.
 * <p>
 * Assuming the generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.json.converter.CobolJsonConverter
 * CobolJsonConverter} can be used like so:
 * 
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis);) {
 * 	CobolJsonConverter&lt;CustomerData&gt; converter = new CobolJsonConverter&lt;&gt;(CustomerData.class);
 * 	StringWriter writer = new StringWriter();
 * 	converter.convert(cis, writer); // Writer will contain the JSON produced
 * }
 * </pre>
 */
module org.legstar.cobol.json.converter {
	
	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.converter;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive com.fasterxml.jackson.core;
	
	exports org.legstar.cobol.json.converter;
	
}