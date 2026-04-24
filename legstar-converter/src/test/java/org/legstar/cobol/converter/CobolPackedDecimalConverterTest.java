package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class CobolPackedDecimalConverterTest extends CobolConverterTestBase {

	CobolPackedDecimalConverter converter = new CobolPackedDecimalConverter(0x40, 0x0c, 0x0d, 0x0f);

	private final HexFormat hex = HexFormat.of().withUpperCase();

	@Test
	public void testNoHostData() {
		try {
			fromHost("", false, 2, 0);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void allLowValues() {
		assertEquals("0", fromHost("0000", false, 2, 0));
	}

	@Test
	public void allSpaceValues() {
		assertEquals("0", fromHost("404040", false, 4, 0));
	}

	@Test
	public void oneDigitUnsigned() {
		assertEquals("5", fromHost("5F", false, 1, 0));
	}

	@Test
	public void oneDigitSigned() {
		assertEquals("-5", fromHost("5D", true, 1, 0));
	}

	@Test
	public void scaling() {
		assertEquals("-12345670", fromHost("1234567D", true, 7, -1));
		assertEquals("-1234567", fromHost("1234567D", true, 7, 0));
		assertEquals("-123456.7", fromHost("1234567D", true, 7, 1));
		assertEquals("-12345.67", fromHost("1234567D", true, 7, 2));
		assertEquals("-1234.567", fromHost("1234567D", true, 7, 3));
		assertEquals("-123.4567", fromHost("1234567D", true, 7, 4));
		assertEquals("-12.34567", fromHost("1234567D", true, 7, 5));
		assertEquals("-1.234567", fromHost("1234567D", true, 7, 6));
		assertEquals("-0.1234567", fromHost("1234567D", true, 7, 7));
		assertEquals("-0.01234567", fromHost("1234567D", true, 7, 8));
	}

	@Test
	public void invalidHighNibble() {
		try {
			fromHost("AD", true, 1, 0);
			fail();
		} catch (Exception e) {
			assertEquals("High nibble 10 at byte position 0 is invalid for a BigDecimal. Not in [0-9] range.",
					e.getMessage());
		}
	}

	@Test
	public void invalidLowNibble() {
		try {
			fromHost("5A", true, 1, 0);
			fail();
		} catch (Exception e) {
			assertEquals("Low nibble 10 at byte position 0 is invalid for a BigDecimal. Not a sign indicator.",
					e.getMessage());
		}
	}

	@Test
	public void toCobolSignum() {
		assertEquals("000F", toHost("0", false, 2, 0));
		assertEquals("000C", toHost("0", true, 2, 0));
		assertEquals("001D", toHost("-1", false, 2, 0));
		assertEquals("001D", toHost("-1", true, 2, 0));
	}
	
	@Test
	public void toCobolScale() {
		assertEquals("1234567F", toHost("12345670", false, 7, -1));
		assertEquals("1234567F", toHost("1234567", false, 7, 0));
		assertEquals("1234567F", toHost("123456.7", false, 7, 1));
		assertEquals("1234567F", toHost("12345.67", false, 7, 2));
		assertEquals("1234567F", toHost("1234.567", false, 7, 3));
		assertEquals("1234567F", toHost("123.4567", false, 7, 4));
		assertEquals("1234567F", toHost("12.34567", false, 7, 5));
		assertEquals("1234567F", toHost("1.234567", false, 7, 6));
		assertEquals("1234567F", toHost("0.1234567", false, 7, 7));
		assertEquals("1234567F", toHost("0.01234567", false, 7, 8));
	}

	@Test
	public void toCobolReScale() {
		assertEquals("1234560F", toHost("123456", false, 7, 1));
		assertEquals("1234500F", toHost("12345", false, 7, 2));
		assertEquals("2345670F", toHost("1234567", false, 7, 1));
		assertEquals("3456700F", toHost("1234567", false, 7, 2));
		assertEquals("0123456F", toHost("123456.7", false, 7, 0));
		assertEquals("0123456F", toHost("12345.67", false, 7, 1));
		assertEquals("0012345F", toHost("123.4567", false, 7, 2));
	}

	private String fromHost(String payload, boolean signed, int totalDigits, int fractionDigits) {
		return converter.toString(inputStreamFrom(payload), signed, totalDigits, fractionDigits);
	}

	private String toHost(String decimal, boolean signed, int totalDigits, int fractionDigits) {
		return hex.formatHex(converter.toCobol(new BigDecimal(decimal), signed, totalDigits, fractionDigits));
	}
}
