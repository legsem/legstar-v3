/**
 * Converters for COBOL data to and from JAVA objects.
 * <p>
 * The prerequisite is to use the legstar-generator to produce a Cobol annotated
 * java bean.
 * <p>
 * Assuming the legstar-generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.converter.CobolBeanConverter CobolBeanConverter} can
 * be used like so:
 * 
 * <h3>From Cobol to Java:</h3>
 * 
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis)) {
 * 	CobolBeanConverter&lt;CustomerData&gt; converter = new CobolBeanConverter&lt;&gt;(CustomerData.class);
 * 	CustomerData bean = converter.toJava(cis); // Produce a converted Java bean instance
 * }
 * </pre>
 * 
 * <h3>From Java to Cobol:</h3>
 * 
 * <pre>
 * try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
 * 		CobolOutputStream cos = new CobolOutputStream(fos);) {
 * 	CustomerData bean = new CustomerData();
 * 	bean.setCustomerId(new BigDecimal("245"));
 * 	CustomerData.PersonalData personalData = new CustomerData.PersonalData();
 * 	personalData.setCustomerName("Jean Dupond");
 * 	personalData.setCustomerAddress("712 Hanna Plains, RoweTown");
 * 	personalData.setCustomerPhone("12345678");
 * 	bean.setPersonalData(personalData);
 * 	CobolBeanConverter&lt;CustomerData&gt; converter = new CobolBeanConverter&lt;&gt;(CustomerData.class);
 * 	converter.toCobol(cos, bean); // Serialize Java bean as Cobol
 * }
 * </pre>
 */
module org.legstar.cobol.converter {

	requires transitive org.legstar.cobol.base;

	exports org.legstar.cobol.converter;

}