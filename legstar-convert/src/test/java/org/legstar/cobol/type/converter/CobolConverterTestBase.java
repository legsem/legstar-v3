package org.legstar.cobol.type.converter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HexFormat;

public class CobolConverterTestBase {

	CobolConverterConfig config = new CobolConverterConfigEbcdic();
	
	public byte[] toBytes(String s) {
		HexFormat hex = HexFormat.of().withUpperCase();
		return hex.parseHex(s);
	}
	
	public InputStream inputStreamFrom(String hex) {
		return new ByteArrayInputStream(toBytes(hex));
	}
 
}
