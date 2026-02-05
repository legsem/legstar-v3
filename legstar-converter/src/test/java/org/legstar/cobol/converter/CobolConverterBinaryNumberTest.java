package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class CobolConverterBinaryNumberTest extends CobolConverterTestBase {

	CobolConverterBinaryNumber converter = new CobolConverterBinaryNumber();

	@Test
	public void testNoHostData() {
		try {
			converter.toShort(inputStreamFrom(""), true, 2);
			fail();
		} catch (Exception e) {
			assertEquals("No more data available", e.getMessage());
		}
	}

	@Test
	public void testNotEnoughHostData() {
		assertEquals((short) 4608, converter.toShort(inputStreamFrom("12"), true, 1));
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
}
