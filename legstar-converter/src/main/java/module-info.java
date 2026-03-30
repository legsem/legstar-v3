/**
 * Converters for COBOL data entries to JAVA objects.
 * <p>
 * The prerequisite is to use the legstar-generator to produce a cobol annotated
 * java bean.
 * <p>
 * Assuming the generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.converter.CobolBeanConverter CobolBeanConverter} can
 * be used like so:
 * 
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis);) {
 * 	CobolBeanConverter&lt;CustomerData&gt; converter = new CobolBeanConverter&lt;&gt;(CustomerData.class);
 * 	CustomerData bean = converter.convert(cis); // A converted Java bean instance
 * }
 * </pre>
 */
module org.legstar.cobol.converter {

	requires transitive org.legstar.cobol.base;

	exports org.legstar.cobol.converter;

}