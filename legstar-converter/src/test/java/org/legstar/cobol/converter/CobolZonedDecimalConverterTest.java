package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class CobolZonedDecimalConverterTest extends CobolConverterTestBase {

	CobolZonedDecimalConverter converter = new CobolZonedDecimalConverter(0x60, 0x4E, 0x0c, 0x0d, 0x0f);

	private final HexFormat hex = HexFormat.of().withUpperCase();

	@Test
	public void testNoHostData() {
		try {
			fromHost("", 2, 0, false, false);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
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
	
	@Test
	public void toCobolSignum() {
		assertEquals("F0F0", toHost("0", false, 2, 0, false, false));
		assertEquals("F0F04E", toHost("0", false, 2, 0, false, true));
		assertEquals("F0F0", toHost("0", false, 2, 0, true, false));
		assertEquals("4EF0F0", toHost("0", false, 2, 0, true, true));
		assertEquals("F0D1", toHost("-1", false, 2, 0, false, false));
		assertEquals("F0F160", toHost("-1", false, 2, 0, false, true));
		assertEquals("D0F1", toHost("-1", false, 2, 0, true, false));
		assertEquals("60F0F1", toHost("-1", false, 2, 0, true, true));
		assertEquals("F0C0", toHost("0", true, 2, 0, false, false));
		assertEquals("F0F04E", toHost("0", true, 2, 0, false, true));
		assertEquals("C0F0", toHost("0", true, 2, 0, true, false));
		assertEquals("4EF0F0", toHost("0", true, 2, 0, true, true));
		assertEquals("F0D1", toHost("-1", true, 2, 0, false, false));
		assertEquals("F0F160", toHost("-1", true, 2, 0, false, true));
		assertEquals("D0F1", toHost("-1", true, 2, 0, true, false));
		assertEquals("60F0F1", toHost("-1", true, 2, 0, true, true));
	}

	@Test
	public void toCobolSize() {
		assertEquals("F0F0F7F2", toHost("72", false, 4, 0, false, false));
		assertEquals("F2F4F5F6", toHost("72456", false, 4, 0, false, false));
		assertEquals("F7F2F4", toHost("724", false, 3, 0, false, false));
	}

	@Test
	public void toCobolScale() {
		assertEquals("F1F2F3F4F5F6F7", toHost("12345670", false, 7, -1, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("1234567", false, 7, 0, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("123456.7", false, 7, 1, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("12345.67", false, 7, 2, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("1234.567", false, 7, 3, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("123.4567", false, 7, 4, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("12.34567", false, 7, 5, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("1.234567", false, 7, 6, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("0.1234567", false, 7, 7, false, false));
		assertEquals("F1F2F3F4F5F6F7", toHost("0.01234567", false, 7, 8, false, false));
	}

	@Test
	public void toCobolReScale() {
		assertEquals("F1F2F3F4F5F6F0", toHost("123456", false, 7, 1, false, false));
		assertEquals("F1F2F3F4F5F0F0", toHost("12345", false, 7, 2, false, false));
		assertEquals("F2F3F4F5F6F7F0", toHost("1234567", false, 7, 1, false, false));
		assertEquals("F3F4F5F6F7F0F0", toHost("1234567", false, 7, 2, false, false));
		assertEquals("F0F1F2F3F4F5F6", toHost("123456.7", false, 7, 0, false, false));
		assertEquals("F0F1F2F3F4F5F6", toHost("12345.67", false, 7, 1, false, false));
		assertEquals("F0F0F1F2F3F4F5", toHost("123.4567", false, 7, 2, false, false));
	}

	private String fromHost(String payload, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {
		return converter.toString(inputStreamFrom(payload), totalDigits, fractionDigits, signLeading, signSeparate);
	}

	private String toHost(String decimal, boolean signed, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {
		return hex.formatHex(converter.toCobol(new BigDecimal(decimal), signed, totalDigits, fractionDigits, signLeading, signSeparate));
	}
}
