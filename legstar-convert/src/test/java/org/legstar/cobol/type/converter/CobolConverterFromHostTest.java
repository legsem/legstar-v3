package org.legstar.cobol.type.converter;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;

import legstar.samples.custdat.CustomerData;

public class CobolConverterFromHostTest extends CobolConverterTestBase {
	

	@Test
	public void t() {
		CobolConverterFromHost<CustomerData> fromHost = new CobolConverterFromHost<>(config);
		CustomerData output = fromHost.convert(inputStreamFrom("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F"), CustomerData.class);
	}
	
}
