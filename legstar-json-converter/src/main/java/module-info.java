/**
 * Converters for COBOL data entries to and from JSON.
 * <p>
 * The pre-requisite is to use the legstar-json-generator to produce a Cobol and
 * JACKSON annotated java bean.
 * <p>
 * Assuming the generator produced a CustomerData.java class, the
 * {@link org.legstar.cobol.json.converter.CobolJsonConverter
 * CobolJsonConverter} can be used like so:
 * <p>
 * <b>From Cobol to JSON:</b>
 * 
 * <pre>
 * try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin"); // Cobol binary data
 * 		CobolInputStream cis = new CobolInputStream(fis);) {
 * 	CobolJsonConverter&lt;CustomerData&gt; converter = new CobolJsonConverter&lt;&gt;(CustomerData.class);
 * 	StringWriter writer = new StringWriter();
 * 	converter.toJson(cis, writer); // Writer will contain the JSON produced
 * }
 * </pre>
 * 
 * <b>From JSON to Cobol:</b>
 * 
 * <pre>
 * try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
 * 		CobolOutputStream cos = new CobolOutputStream(fos);) {
 * 	CobolJsonConverter&lt;CustomerData&gt; converter = new CobolJsonConverter&lt;&gt;(CustomerData.class);
 * 	String json = """
 * 			{
 * 			    "customerId": 2,
 * 			    "personalData": {
 * 			        "customerName": "FRED BROWN",
 * 			        "customerAddress": "CAMBRIDGE",
 * 			        "customerPhone": "38791206"
 * 			    },
 * 			    "transactions": {
 * 			        "transactionNbr": 2,
 * 			        "transaction": [
 * 			            {
 * 			                "transactionDateChoice": {
 * 			                    "transactionDate": "10/05/26"
 * 			                },
 * 			                "transactionAmount": 36.82,
 * 			                "transactionComment": "*********"
 * 			            },
 * 			            {
 * 			                "transactionDateChoice": {
 * 			                    "transactionDate": "13/07/26"
 * 			                },
 * 			                "transactionAmount": 175.93,
 * 			                "transactionComment": "*********"
 * 			            }
 * 			        ]
 * 			    }
 * 			}
 * 			""";
 * 	converter.toCobol(cos, new StringReader(json)); // Serialize JSON as Cobol
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