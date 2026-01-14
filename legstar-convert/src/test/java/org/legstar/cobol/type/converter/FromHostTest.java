package org.legstar.cobol.type.converter;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;

import legstar.samples.custdat.CustomerData;

public class FromHostTest {
	
	private FromHost<CustomerData> fromHost = new FromHost<>();

	@Test
	public void t() {
		byte[] payload = toBytes("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F");
		CustomerData output = fromHost.convert(payload, new CustomerData());
	}
	
	private byte[] toBytes(String s) {
		HexFormat hex = HexFormat.of().withUpperCase();
		return hex.parseHex(s);
	}
 
}
