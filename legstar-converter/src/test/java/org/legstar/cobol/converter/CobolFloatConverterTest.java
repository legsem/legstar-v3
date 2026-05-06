package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

public class CobolFloatConverterTest extends CobolConverterTestBase {

	CobolFloatConverter converter = new CobolFloatConverter();

	@Test
	public void testNoHostData() {
		try {
			converter.toFloat(inputStreamFrom(""));
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
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

	@Test
	public void testToHostFloat() {
		assertEquals(-118.625f, toHost(-118.625f));
		assertEquals(25431.121f, toHost(25431.121f));
		assertEquals(345006.56f, toHost(345006.56f));
		assertEquals(128f, toHost(128));
		assertEquals(-128f, toHost(-128));
		assertEquals(0f, toHost(0));
		assertEquals(0f, toHost(-0));
		assertEquals(1.0f, toHost(1.0f));
		assertEquals(0.1f, toHost(0.1f), 0.0000001f);
		assertEquals(5.23E-5, toHost(5.23E-5f), 0.000001E-5f);
		assertEquals(-5.670078E-14, toHost(-5.670078E-14f), 0.000001E-14f);
		assertEquals(7.982006699999995E-14, toHost(7.982006699999995E-14f), 0.00001E-14f);
		assertEquals(Float.MAX_VALUE, toHost(Float.MAX_VALUE));
		assertEquals(Float.MIN_NORMAL, toHost(Float.MIN_NORMAL));
		assertEquals(Float.MIN_VALUE, toHost(Float.MIN_VALUE));
		assertEquals(1E-39f, toHost(1E-39f), 0.00001E-39f);
	}

	@Test
	public void testToHostFloatErrors() {
		toHostError(Float.NaN, "Unsupported float NaN");
		toHostError(Float.NEGATIVE_INFINITY, "Unsupported float -Infinity");
		toHostError(Float.POSITIVE_INFINITY, "Unsupported float Infinity");
	}

	private Float fromHost(String payload) {
		return converter.toFloat(inputStreamFrom(payload));
	}

	private float toHost(float value) {
		return fromComp_1(converter.toCobol(value));
	}
	
	private void toHostError(float value, String expectedMsg) {
		try {
			toHost(value);
			fail();
		} catch (Exception e) {
			assertEquals(expectedMsg, e.getMessage());
		}
	}

	/**
	 * Evaluate the actual value of a COMP-1 numeric.
	 * 
	 * @param comp_1 the COMP-1
	 * @return the numeric value of the COMP-1 in power of 10
	 */
	private float fromComp_1(byte[] comp_1) {
		ByteBuffer bb = ByteBuffer.wrap(comp_1);
		int bitsComp_1 = bb.getInt();
		int sign = CobolFloatConverter.sign(bitsComp_1);
		int exponent = CobolFloatConverter.comp_1Exponent(bitsComp_1);
		int mantissa = CobolFloatConverter.comp_1Mantissa(bitsComp_1);
		float fraction = 0f;
		int i = 8;
		for (int p = -1; p > -7; p--) {
			fraction += ((mantissa << i) >>> 28) * Math.pow(16,  p);
			i = i + 4;
		}
		return (float) (Math.pow(-1, sign) * fraction * Math.pow(16, exponent));
	}

}
