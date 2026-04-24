package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class CobolStringConverterTest extends CobolConverterTestBase {


	private final HexFormat hex = HexFormat.of().withUpperCase();
	
	@Test
	public void testToStringNoHostData() {
		try {
			toString("", 1);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void testToString0ByteLen() {
		assertEquals("", toString("D5C1D4C5F0", 0));
	}

	@Test
	public void testToString1ByteLen() {
		assertEquals("N", toString("D5C1D4C5F0", 1));
	}

	@Test
	public void testToString5ByteLen() {
		assertEquals("NAME0", toString("D5C1D4C5F0", 5));
	}

	@Test
	public void testToString6ByteLen() {
		assertEquals("NAME0", toString("D5C1D4C5F0", 6));
	}

	@Test
	public void testToStringRemoveLowAndHighValues() {
		assertEquals("NM0", toString("D500D4FFF0", 5));
	}

	@Test
	public void testToStringTruncateTrailingSpace() {
		assertEquals("NAM", toString("D5C1D44040", 5, true));
		assertEquals("", toString("4040404040", 5, true));
	}
	
	@Test
	public void testToCobolEmptyString() {
		assertEquals("", toCobol("", 0));
		assertEquals("0000000000", toCobol("", 5));
	}

	@Test
	public void testToCobolUnderflow() {
		assertEquals("D5C1D4C5", toCobol("NAME", 4));
		assertEquals("D5C1D4C500", toCobol("NAME", 5));
		assertEquals("D5C1D4C50000", toCobol("NAME", 6));
	}

	@Test
	public void testToCobolOverflow() {
		assertEquals("D5C1D4C5", toCobol("NAME0", 4));
		assertEquals("D5C1D4C5", toCobol("NAME01", 4));
		assertEquals("D5C1D4C5", toCobol("NAME02", 4));
	}

	@Test
	public void testToCobolPadWithSpaces() {
		assertEquals("D5C1D4C5", toCobol("NAME", 4, true));
		assertEquals("D5C1D4C540", toCobol("NAME", 5, true));
		assertEquals("D5C1D4C54040", toCobol("NAME", 6, true));
	}

	private String toString(String hex, int size) {
		return toString(hex, size, false);
	}

	private String toString(String hex, int size, boolean truncateHostStringsTrailingSpaces) {
		CobolStringConverter converter = new CobolStringConverter("IBM01140", truncateHostStringsTrailingSpaces, false,
				64);
		return converter.toString(inputStreamFrom(hex), size);
	}

	private String toCobol(String s, int size) {
		return toCobol(s, size, false);
	}

	private String toCobol(String s, int size, boolean rightPadCobolAlphanumWithSpaces) {
		CobolStringConverter converter = new CobolStringConverter("IBM01140", false, rightPadCobolAlphanumWithSpaces,
				64);
		byte[] res = converter.toCobol(s, size);
		return hex.formatHex(res);
	}


}
