package org.legstar.cobol.type.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BytesLenUtilsTest {
	
	@Test
	public void testBinaryNumberByteLen() {
		assertEquals(2, BytesLenUtils.binaryNumberByteLen(1));
		assertEquals(2, BytesLenUtils.binaryNumberByteLen(2));
		assertEquals(2, BytesLenUtils.binaryNumberByteLen(4));
		assertEquals(4, BytesLenUtils.binaryNumberByteLen(5));
		assertEquals(4, BytesLenUtils.binaryNumberByteLen(9));
		assertEquals(8, BytesLenUtils.binaryNumberByteLen(10));
		assertEquals(8, BytesLenUtils.binaryNumberByteLen(18));
		assertEquals(8, BytesLenUtils.binaryNumberByteLen(19)); // no support for extended arithmetic
	}

	@Test
	public void testPackedDecimalByteLen() {
		assertEquals(1, BytesLenUtils.packedDecimalByteLen(1));
		assertEquals(2, BytesLenUtils.packedDecimalByteLen(2));
		assertEquals(2, BytesLenUtils.packedDecimalByteLen(3));
		assertEquals(3, BytesLenUtils.packedDecimalByteLen(5));
		assertEquals(4, BytesLenUtils.packedDecimalByteLen(6));
		assertEquals(5, BytesLenUtils.packedDecimalByteLen(9));
		assertEquals(6, BytesLenUtils.packedDecimalByteLen(10));
		assertEquals(9, BytesLenUtils.packedDecimalByteLen(17));
		assertEquals(10, BytesLenUtils.packedDecimalByteLen(18));
		
	}

	@Test
	public void testZonedDecimalByteLen() {
		assertEquals(1, BytesLenUtils.zonedDecimalByteLen(1, false));
		assertEquals(2, BytesLenUtils.zonedDecimalByteLen(1, true));
		assertEquals(6, BytesLenUtils.zonedDecimalByteLen(6, false));
		assertEquals(7, BytesLenUtils.zonedDecimalByteLen(6, true));
	}

}
