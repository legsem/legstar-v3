package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.converter.testbase.CobolConverterTestBase;

public class CobolStringConverterTest extends CobolConverterTestBase {

	CobolStringConverter converter = new CobolStringConverter("IBM01140", false);

	@Test
	public void testNoHostData() {
		try {
			converter.toString(inputStreamFrom(""), 1);
			fail();
		} catch (CobolBeanConverterEOFException e) {
			assertEquals("Not enough cobol input data available", e.getMessage());
		}
	}

	@Test
	public void test0ByteLen() {
		assertEquals("", converter.toString(inputStreamFrom("D5C1D4C5F0"), 0));
	}

	@Test
	public void test1ByteLen() {
		assertEquals("N", converter.toString(inputStreamFrom("D5C1D4C5F0"), 1));
	}

	@Test
	public void test5ByteLen() {
		assertEquals("NAME0", converter.toString(inputStreamFrom("D5C1D4C5F0"), 5));
	}

	@Test
	public void test6ByteLen() {
		assertEquals("NAME0", converter.toString(inputStreamFrom("D5C1D4C5F0"), 6));
	}

	@Test
	public void testRemoveLowAndHighValues() {
		assertEquals("NM0", converter.toString(inputStreamFrom("D500D4FFF0"), 5));
	}
}
