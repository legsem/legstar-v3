/**
 * Converters for COBOL data entries to XML.
 * <p>
 * The prerequisite is to use the legstar-jaxb-generator to produce a cobol and
 * JAXB annotated java bean.
 * <p>
 * Assuming the generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.jaxb.converter.CobolJaxbConverter
 * CobolJaxbConverter} can be used like so:
 * <p> 
 * <b>From Cobol to XML:</b>
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis)) {
 * 	CobolJaxbConverter&lt;CustomerData&gt; converter = new CobolJaxbConverter&lt;&gt;(CustomerData.class);
 * 	StringWriter writer = new StringWriter();
 * 	converter.toXml(cis, writer); // Writer will contain the XML produced
 * }
 * </pre>
 * <b>From XML to Cobol:</b>
 * <pre>
 * try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
 *         CobolOutputStream cos = new CobolOutputStream(fos)) {
 *     CobolJaxbConverter&lt;CustomerData&gt; converter = new CobolJaxbConverter&lt;&gt;(CustomerData.class);
 *     String xml = """
 *             &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 *             &lt;customerData&gt;
 *                 &lt;customerId&gt;1&lt;/customerId&gt;
 *                 &lt;personalData&gt;
 *                     &lt;customerName&gt;JOHN SMITH          &lt;/customerName&gt;
 *                     &lt;customerAddress&gt;CAMBRIDGE UNIVERSITY&lt;/customerAddress&gt;
 *                     &lt;customerPhone&gt;44012565&lt;/customerPhone&gt;
 *                 &lt;/personalData&gt;
 *                 &lt;transactions&gt;
 *                     &lt;transactionNbr&gt;2&lt;/transactionNbr&gt;
 *                     &lt;transaction&gt;
 *                         &lt;transactionDateChoice&gt;
 *                             &lt;transactionDate&gt;10/05/26&lt;/transactionDate&gt;
 *                         &lt;/transactionDateChoice&gt;
 *                         &lt;transactionAmount&gt;235.56&lt;/transactionAmount&gt;
 *                         &lt;transactionComment&gt;*********&lt;/transactionComment&gt;
 *                     &lt;/transaction&gt;
 *                     &lt;transaction&gt;
 *                         &lt;transactionDateChoice&gt;
 *                             &lt;transactionDate&gt;13/07/26&lt;/transactionDate&gt;
 *                         &lt;/transactionDateChoice&gt;
 *                         &lt;transactionAmount&gt;-56.65&lt;/transactionAmount&gt;
 *                         &lt;transactionComment&gt;*********&lt;/transactionComment&gt;
 *                     &lt;/transaction&gt;
 *                 &lt;/transactions&gt;
 *             &lt;/customerData&gt;
 *             """;
 *     converter.toCobol(cos, new StreamSource(new StringReader(xml))); // Serialize Xml as Cobol
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