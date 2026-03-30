/**
 * Converters for COBOL data entries to XML.
 * <p>
 * The prerequisite is to use the legstar-jaxb-generator to produce a cobol and
 * JAXB annotated java bean.
 * <p>
 * Assuming the generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.jaxb.converter.CobolJaxbConverter
 * CobolJaxbConverter} can be used like so:
 * 
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis);) {
 * 	CobolJaxbConverter&lt;CustomerData&gt; converter = new CobolJaxbConverter&lt;&gt;(CustomerData.class);
 * 	StringWriter writer = new StringWriter();
 * 	converter.convert(cis, writer); // Writer will contain the XML produced
 * }
 * </pre>
 */
module org.legstar.cobol.jaxb.converter {

	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.converter;
	requires transitive jakarta.xml.bind;
	requires transitive org.glassfish.jaxb.runtime;

	exports org.legstar.cobol.jaxb.converter;

}