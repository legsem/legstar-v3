package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class CobolConverterFloatTest extends CobolConverterTestBase {

	CobolConverterFloat converter = new CobolConverterFloat(config);

	@Test
	public void testNoHostData() {
		try {
			converter.toFloat(inputStreamFrom(""));
			fail();
		} catch (Exception e) {
			assertEquals("Not enough bytes for a float. Needed 4, got -1 instead", e.getMessage());
		}
	}

    @Test
    public void testFromHostFloat() {
        assertEquals(1234.0f, fromHost("434d2000"));
        assertEquals(0.0f, fromHost("00000000"));
        assertEquals(1.0f, fromHost("41100000"));
        assertEquals(345006.56f, fromHost("45543ae9"));
        assertEquals(7.982005E-14f, fromHost("361677a4"));
        assertEquals(3.4028235E38f, fromHost("60ffffff"));
        assertEquals(25431.121f, fromHost("4463571f"));
        assertEquals(0.070859075f, fromHost("401223d2"));
        assertEquals(0.2050727f, fromHost("40347fa5"));
        assertEquals(0.21563375f, fromHost("403733c6"));
        assertEquals(0.86346835f, fromHost("40dd0c43"));
        assertEquals(0.31136322f, fromHost("404fb580"));
        assertEquals(0.63990897f, fromHost("40a3d113"));
        assertEquals(0.7430732f, fromHost("40be3a0c"));
        assertEquals(0.8783575f, fromHost("40e0dc0a"));
        assertEquals(0.03406465f, fromHost("3f8b8760"));
        assertEquals(0.99642426f, fromHost("40ff15a9"));
    }

	private Float fromHost(String payload) {
		return converter.toFloat(inputStreamFrom(payload));
	}
}
