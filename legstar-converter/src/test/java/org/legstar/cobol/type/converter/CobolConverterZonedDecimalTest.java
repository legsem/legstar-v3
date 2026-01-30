package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class CobolConverterZonedDecimalTest extends CobolConverterTestBase {

	CobolConverterZonedDecimal converter = new CobolConverterZonedDecimal(config);

	@Test
	public void testNoHostData() {
		try {
			fromHost("", 2, 0, false, false);
			fail();
		} catch (Exception e) {
			assertEquals("No more data available", e.getMessage());
		}
	}

	@Test
	public void allLowValues() {
		assertEquals("0", fromHost("00000000", 4, 0, false, false));
	}

	@Test
	public void allZeroes() {
		assertEquals("0", fromHost("F0F0", 2, 0, false, false));
	}

	@Test
	public void allSpaces() {
		assertEquals("0", fromHost("40404040", 4, 0, false, false));
	}

	@Test
	public void overpunchPlusTrailing() {
		assertEquals("1234", fromHost("F1F2F3C4", 4, 0, false, false));
	}

	@Test
	public void overpunchMinusTrailing() {
		assertEquals("-1234", fromHost("F1F2F3D4", 4, 0, false, false));
	}

	@Test
	public void overpunchPlusLeading() {
		assertEquals("1234", fromHost("C1F2F3F4", 4, 0, true, false));
	}

	@Test
	public void overpunchMinusLeading() {
		assertEquals("-1234", fromHost("D1F2F3F4", 4, 0, true, false));
	}

	@Test
	public void separatePlusTrailing() {
		assertEquals("1234", fromHost("F1F2F3F44E", 4, 0, false, true));
	}

	@Test
	public void separateMinusTrailing() {
		assertEquals("-1234", fromHost("F1F2F3F460", 4, 0, false, true));
	}

	@Test
	public void separatePlusLeading() {
		assertEquals("1234", fromHost("4EF1F2F3F4", 4, 0, true, true));
	}

	@Test
	public void separateMinusLeading() {
		assertEquals("-1234", fromHost("60F1F2F3F4", 4, 0, true, true));
	}

	@Test
	public void scale1() {
		assertEquals("123.4", fromHost("F1F2F3C4", 4, 1, false, false));
	}

	@Test
	public void scale2() {
		assertEquals("12.34", fromHost("F1F2F3C4", 4, 2, false, false));
	}
	
	@Test
	public void scale4() {
		assertEquals("0.1234", fromHost("F1F2F3C4", 4, 4, false, false));
	}
	
	@Test
	public void scale5() {
		assertEquals("0.01234", fromHost("F1F2F3C4", 4, 5, false, false));
	}
	
	private String fromHost(String payload, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {
		return converter.toString(inputStreamFrom(payload), totalDigits, fractionDigits, signLeading, signSeparate);
	}
}
