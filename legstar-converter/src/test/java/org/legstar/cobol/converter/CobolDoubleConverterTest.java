package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

public class CobolDoubleConverterTest extends CobolConverterTestBase {

	CobolDoubleConverter converter = new CobolDoubleConverter();

	@Test
	public void testNoHostData() {
		try {
			converter.toDouble(inputStreamFrom(""));
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void testFromHostDouble() {
		assertEquals(0.0d, fromHost("0000000000000000"));
		assertEquals(1.0d, fromHost("4110000000000000"));
		assertEquals(345006.56779999996d, fromHost("45543ae915b573e8"));
		assertEquals(7.982006699999995E-14d, fromHost("361677a4590fab68"));
		assertEquals(3.40282347E38d, fromHost("60ffffff048ff9e8"));
		assertEquals(-5.670078E-14d, fromHost("b5ff5b8c70649ed0"));
		assertEquals(0.5160421914381664d, fromHost("40841b574f9545cd"));
		assertEquals(0.3767408804881589d, fromHost("4060721720c34c90"));
		assertEquals(0.5748594107197613d, fromHost("409329fc80d6d646"));
		assertEquals(0.6001057816958458d, fromHost("4099a0885286303d"));
		assertEquals(0.7850369597199326d, fromHost("40c8f82ea425fc38"));
		assertEquals(0.2998016949252067d, fromHost("404cbfcdcafd37c0"));
		assertEquals(0.7788391667528523d, fromHost("40c76200ee0c21d0"));
		assertEquals(0.8158852356638823d, fromHost("40d0dddad4773358"));
		assertEquals(0.4364272923152456d, fromHost("406fb9b2f3936868"));
		assertEquals(0.6062248297052384d, fromHost("409b318ce99b6f60"));
	}

	@Test
	public void testToHostDouble() {
		assertEquals(-118.625d, toHost(-118.625));
		assertEquals(128d, toHost(128));
		assertEquals(-128d, toHost(-128));
		assertEquals(0d, toHost(0));
		assertEquals(0d, toHost(-0));
		assertEquals(1.0d, toHost(1.0));
		assertEquals(0.1d, toHost(0.1), 0.0000000000001);
		assertEquals(5.23E-5, toHost(5.23E-5), 0.00000000000001E-5);
		assertEquals(-5.670078E-14, toHost(-5.670078E-14));
		assertEquals(3.40282347E38, toHost(3.40282347E38), 0.000000000000001E38);
		assertEquals(7.982006699999995E-14, toHost(7.982006699999995E-14), 0.0000000000001E-14);
	}

	@Test
	public void testToHostDoubleErrors() {
		toHostError(Double.NaN, "Unsupported double NaN");
		toHostError(Double.NEGATIVE_INFINITY, "Unsupported double -Infinity");
		toHostError(Double.POSITIVE_INFINITY, "Unsupported double Infinity");
		toHostError(Double.MAX_VALUE, "Hexadecimal biased exponent 320 outside supported range (0-127)");
		toHostError(Double.MIN_NORMAL, "Hexadecimal biased exponent -191 outside supported range (0-127)");
		toHostError(Double.MIN_VALUE, "Subnormal doubles are not supported");
		toHostError(1E-99, "Hexadecimal biased exponent -18 outside supported range (0-127)");
	}

	private Double fromHost(String payload) {
		return converter.toDouble(inputStreamFrom(payload));
	}

	private double toHost(double value) {
		return fromComp_2(converter.toCobol(value));
	}
	
	private void toHostError(double value, String expectedMsg) {
		try {
			toHost(value);
			fail();
		} catch (Exception e) {
			assertEquals(expectedMsg, e.getMessage());
		}
	}

	/**
	 * Evaluate the actual value of a COMP-2 numeric.
	 * 
	 * @param comp_2 the COMP-2
	 * @return the numeric value of the COMP-2 in power of 10
	 */
	private double fromComp_2(byte[] comp_2) {
		ByteBuffer bb = ByteBuffer.wrap(comp_2);
		long bitsComp_2 = bb.getLong();
		long sign = CobolDoubleConverter.sign(bitsComp_2);
		long exponent = CobolDoubleConverter.comp_2Exponent(bitsComp_2);
		long mantissa = CobolDoubleConverter.comp_2Mantissa(bitsComp_2);
		double fraction = 0d;
		int i = 8;
		for (int p = -1; p > -14; p--) {
			long left = mantissa << i;
			long right = left >>> 60;
			fraction += right * Math.pow(16, p);
			i = i + 4;
		}
		return Math.pow(-1, sign) * (fraction) * Math.pow(16, exponent);
	}
}
