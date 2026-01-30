package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class CobolConverterPackedDecimalTest extends CobolConverterTestBase {

	CobolConverterPackedDecimal converter = new CobolConverterPackedDecimal(config);

	@Test
	public void testNoHostData() {
		try {
			fromHost("", false, 2, 0);
			fail();
		} catch (Exception e) {
			assertEquals("No more data available", e.getMessage());
		}
	}

	@Test
	public void allLowValues() {
		assertEquals("0", fromHost("00", false, 2, 0));
	}

	@Test
	public void allSpaceValues() {
		assertEquals("0", fromHost("4040", false, 4, 0));
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
			assertEquals("High nibble 10 at byte position 0 is invalid for a BigDecimal. Not in [0-9] range.", e.getMessage());
		}
	}
	
	@Test
	public void invalidLowNibble() {
		try {
			fromHost("5A", true, 1, 0);
			fail();
		} catch (Exception e) {
			assertEquals("Low nibble 10 at byte position 0 is invalid for a BigDecimal. Not a sign indicator.", e.getMessage());
		}
	}

	private String fromHost(String payload, boolean signed, int totalDigits, int fractionDigits) {
		return converter.toString(inputStreamFrom(payload), signed, totalDigits, fractionDigits);
	}

}
