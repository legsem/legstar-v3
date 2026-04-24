package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class CobolBinaryNumberConverterTest extends CobolConverterTestBase {

	CobolBinaryNumberConverter converter = new CobolBinaryNumberConverter();

	private final HexFormat hex = HexFormat.of().withUpperCase();
	
	@Test
	public void testNoHostData() {
		try {
			converter.toShort(inputStreamFrom(""), true, 2);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void testNotEnoughHostData() {
		try {
			converter.toShort(inputStreamFrom("12"), true, 1);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void testToShort() {
		assertEquals((short) 0, converter.toShort(inputStreamFrom("0000"), true, 4));
		assertEquals((short) 127, converter.toShort(inputStreamFrom("007f"), true, 4));
		assertEquals((short) 32767, converter.toShort(inputStreamFrom("7fff"), true, 4));
		assertEquals((short) -32768, converter.toShort(inputStreamFrom("8000"), true, 4));
		assertEquals((short) -1, converter.toShort(inputStreamFrom("ffff"), true, 4));
	}

	@Test
	public void testToInteger() {
		assertEquals(0, converter.toInteger(inputStreamFrom("0000"), true, 4));
		assertEquals(127, converter.toInteger(inputStreamFrom("007f"), true, 4));
		assertEquals(32767, converter.toInteger(inputStreamFrom("7fff"), true, 4));
		assertEquals(-32768, converter.toInteger(inputStreamFrom("8000"), true, 4));
		assertEquals(-1, converter.toInteger(inputStreamFrom("ffff"), true, 4));
		assertEquals(32767, converter.toInteger(inputStreamFrom("7fff"), false, 4));
		assertEquals(32768, converter.toInteger(inputStreamFrom("8000"), false, 4));
		assertEquals(65535, converter.toInteger(inputStreamFrom("ffff"), false, 4));
		assertEquals( 0, converter.toInteger(inputStreamFrom("00000000"), true, 9));
		assertEquals(127, converter.toInteger(inputStreamFrom("0000007f"), true, 9));
		assertEquals(32767, converter.toInteger(inputStreamFrom("00007fff"), true, 9));
		assertEquals(32768, converter.toInteger(inputStreamFrom("00008000"), true, 9));
		assertEquals(32769, converter.toInteger(inputStreamFrom("00008001"), true, 9));
		assertEquals(65535, converter.toInteger(inputStreamFrom("0000ffff"), true, 9));
		assertEquals(524288, converter.toInteger(inputStreamFrom("00080000"), true, 9));
		assertEquals(2147483647, converter.toInteger(inputStreamFrom("7fffffff"), true, 9));
		assertEquals(-2147483648, converter.toInteger(inputStreamFrom("80000000"), true, 9));
	}

	@Test
	public void testToLong() {
		assertEquals(0, converter.toLong(inputStreamFrom("0000"), true, 4));
		assertEquals(127, converter.toLong(inputStreamFrom("007f"), true, 4));
		assertEquals(32767, converter.toLong(inputStreamFrom("7fff"), true, 4));
		assertEquals(-32768, converter.toLong(inputStreamFrom("8000"), true, 4));
		assertEquals(-1, converter.toLong(inputStreamFrom("ffff"), true, 4));
		assertEquals(32767, converter.toLong(inputStreamFrom("7fff"), false, 4));
		assertEquals(32768, converter.toLong(inputStreamFrom("8000"), false, 4));
		assertEquals(65535, converter.toLong(inputStreamFrom("ffff"), false, 4));
		assertEquals( 0, converter.toLong(inputStreamFrom("00000000"), true, 9));
		assertEquals(127, converter.toLong(inputStreamFrom("0000007f"), true, 9));
		assertEquals(32767, converter.toLong(inputStreamFrom("00007fff"), true, 9));
		assertEquals(32768, converter.toLong(inputStreamFrom("00008000"), true, 9));
		assertEquals(32769, converter.toLong(inputStreamFrom("00008001"), true, 9));
		assertEquals(65535, converter.toLong(inputStreamFrom("0000ffff"), true, 9));
		assertEquals(524288, converter.toLong(inputStreamFrom("00080000"), true, 9));
		assertEquals(2147483647, converter.toLong(inputStreamFrom("7fffffff"), true, 9));
		assertEquals(-2147483648, converter.toLong(inputStreamFrom("80000000"), true, 9));
		assertEquals(2147483647, converter.toLong(inputStreamFrom("7fffffff"), false, 9));
		assertEquals(2147483648l, converter.toLong(inputStreamFrom("80000000"), false, 9));
		assertEquals(0, converter.toLong(inputStreamFrom("0000000000000000"), true, 18));
		assertEquals(74, converter.toLong(inputStreamFrom("000000000000004a"), true, 18));
		assertEquals(-4294967298l, converter.toLong(inputStreamFrom("fffffffefffffffe"), true, 18));
		assertEquals(-1, converter.toLong(inputStreamFrom("ffffffffffffffff"), true, 18));
		assertEquals(-9223372036854775808l, converter.toLong(inputStreamFrom("8000000000000000"), true, 18));
		assertEquals(-75, converter.toLong(inputStreamFrom("ffffffffffffffb5"), true, 18));
		assertEquals(-4294967294l, converter.toLong(inputStreamFrom("ffffffff00000002"), true, 18));
		assertEquals(17179869183l, converter.toLong(inputStreamFrom("00000003ffffffff"), true, 18));
		assertEquals(9223372036854775807l, converter.toLong(inputStreamFrom("7fffffffffffffff"), true, 18));
	}

	@Test
	public void testToCobolShort() {
		assertEquals("0000", hex.formatHex((converter.toCobol((short) 0, 2))));
		assertEquals("0001", hex.formatHex((converter.toCobol((short) 1, 2))));
		assertEquals("004A", hex.formatHex((converter.toCobol((short) 74, 2))));
		assertEquals("7FFF", hex.formatHex((converter.toCobol((short) 32767, 2))));
		assertEquals("FFFF", hex.formatHex((converter.toCobol((short) -1, 2))));
		assertEquals("FFB6", hex.formatHex((converter.toCobol((short) -74, 2))));
		assertEquals("8000", hex.formatHex((converter.toCobol((short) -32768, 2))));
		assertEquals("00000000", hex.formatHex((converter.toCobol((short) 0, 5))));
		assertEquals("00000001", hex.formatHex((converter.toCobol((short) 1, 5))));
		assertEquals("0000004A", hex.formatHex((converter.toCobol((short) 74, 5))));
		assertEquals("00007FFF", hex.formatHex((converter.toCobol((short) 32767, 5))));
		assertEquals("FFFFFFFF", hex.formatHex((converter.toCobol((short) -1, 5))));
		assertEquals("FFFFFFB6", hex.formatHex((converter.toCobol((short) -74, 5))));
		assertEquals("FFFF8000", hex.formatHex((converter.toCobol((short) -32768, 5))));
		assertEquals("0000000000000000", hex.formatHex((converter.toCobol((short) 0, 10))));
		assertEquals("0000000000000001", hex.formatHex((converter.toCobol((short) 1, 10))));
		assertEquals("000000000000004A", hex.formatHex((converter.toCobol((short) 74, 10))));
		assertEquals("0000000000007FFF", hex.formatHex((converter.toCobol((short) 32767, 10))));
		assertEquals("FFFFFFFFFFFFFFFF", hex.formatHex((converter.toCobol((short) -1, 10))));
		assertEquals("FFFFFFFFFFFFFFB6", hex.formatHex((converter.toCobol((short) -74, 10))));
		assertEquals("FFFFFFFFFFFF8000", hex.formatHex((converter.toCobol((short) -32768, 10))));
	}

	@Test
	public void testToCobolInteger() {
		assertEquals("0000", hex.formatHex((converter.toCobol(0, 2))));
		assertEquals("0001", hex.formatHex((converter.toCobol(1, 2))));
		assertEquals("004A", hex.formatHex((converter.toCobol(74, 2))));
		assertEquals("FFFF", hex.formatHex((converter.toCobol(2147483647, 2))));
		assertEquals("FFFF", hex.formatHex((converter.toCobol(-1, 2))));
		assertEquals("FFB6", hex.formatHex((converter.toCobol(-74, 2))));
		assertEquals("0000", hex.formatHex((converter.toCobol(-2147483648, 2))));
		assertEquals("00000000", hex.formatHex((converter.toCobol(0, 5))));
		assertEquals("00000001", hex.formatHex((converter.toCobol(1, 5))));
		assertEquals("0000004A", hex.formatHex((converter.toCobol(74, 5))));
		assertEquals("7FFFFFFF", hex.formatHex((converter.toCobol(2147483647, 5))));
		assertEquals("FFFFFFFF", hex.formatHex((converter.toCobol(-1, 5))));
		assertEquals("FFFFFFB6", hex.formatHex((converter.toCobol(-74, 5))));
		assertEquals("80000000", hex.formatHex((converter.toCobol(-2147483648, 5))));
		assertEquals("0000000000000000", hex.formatHex((converter.toCobol(0, 10))));
		assertEquals("0000000000000001", hex.formatHex((converter.toCobol(1, 10))));
		assertEquals("000000000000004A", hex.formatHex((converter.toCobol(74, 10))));
		assertEquals("000000007FFFFFFF", hex.formatHex((converter.toCobol(2147483647, 10))));
		assertEquals("FFFFFFFFFFFFFFFF", hex.formatHex((converter.toCobol(-1, 10))));
		assertEquals("FFFFFFFFFFFFFFB6", hex.formatHex((converter.toCobol(-74, 10))));
		assertEquals("FFFFFFFF80000000", hex.formatHex((converter.toCobol(-2147483648, 10))));
	}

	@Test
	public void testToCobolLong() {
		assertEquals("0000", hex.formatHex((converter.toCobol(0l, 2))));
		assertEquals("0001", hex.formatHex((converter.toCobol(1l, 2))));
		assertEquals("004A", hex.formatHex((converter.toCobol(74l, 2))));
		assertEquals("FFFF", hex.formatHex((converter.toCobol(9223372036854775807l, 2))));
		assertEquals("FFFF", hex.formatHex((converter.toCobol(-1l, 2))));
		assertEquals("FFB6", hex.formatHex((converter.toCobol(-74l, 2))));
		assertEquals("0000", hex.formatHex((converter.toCobol(-9223372036854775808l, 2))));
		assertEquals("00000000", hex.formatHex((converter.toCobol(0l, 5))));
		assertEquals("00000001", hex.formatHex((converter.toCobol(1l, 5))));
		assertEquals("0000004A", hex.formatHex((converter.toCobol(74l, 5))));
		assertEquals("FFFFFFFF", hex.formatHex((converter.toCobol(9223372036854775807l, 5))));
		assertEquals("FFFFFFFF", hex.formatHex((converter.toCobol(-1l, 5))));
		assertEquals("FFFFFFB6", hex.formatHex((converter.toCobol(-74l, 5))));
		assertEquals("00000000", hex.formatHex((converter.toCobol(-9223372036854775808l, 5))));
		assertEquals("0000000000000000", hex.formatHex((converter.toCobol(0l, 10))));
		assertEquals("0000000000000001", hex.formatHex((converter.toCobol(1l, 10))));
		assertEquals("000000000000004A", hex.formatHex((converter.toCobol(74l, 10))));
		assertEquals("7FFFFFFFFFFFFFFF", hex.formatHex((converter.toCobol(9223372036854775807l, 10))));
		assertEquals("FFFFFFFFFFFFFFFF", hex.formatHex((converter.toCobol(-1l, 10))));
		assertEquals("FFFFFFFFFFFFFFB6", hex.formatHex((converter.toCobol(-74l, 10))));
		assertEquals("8000000000000000", hex.formatHex((converter.toCobol(-9223372036854775808l, 10))));
	}

}
