package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CobolConverterBinaryTest extends CobolConverterTestBase {

	CobolConverterBinary converter = new CobolConverterBinary(config);

	@Test
	public void testNoHostData() {
		assertEquals((short) 0, converter.toShort(inputStreamFrom(""), true, 0));
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
	}
}
