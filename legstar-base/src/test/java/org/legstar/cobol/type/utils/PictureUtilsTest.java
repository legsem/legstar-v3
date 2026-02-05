package org.legstar.cobol.type.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.utils.PictureUtils;

public class PictureUtilsTest {
	
	@Test
	public void testCompNumeric() {
		assertEquals("CompNumeric[signed=false, totalDigits=1, fractionDigits=0]", PictureUtils.compNumeric("9").toString());
		assertEquals("CompNumeric[signed=true, totalDigits=2, fractionDigits=1]", PictureUtils.compNumeric("S9V9").toString());
		assertEquals("CompNumeric[signed=true, totalDigits=7, fractionDigits=2]", PictureUtils.compNumeric("S9(5)V99").toString());
		assertEquals("CompNumeric[signed=true, totalDigits=4, fractionDigits=4]", PictureUtils.compNumeric("SV9999").toString());
	}

	@Test
	public void testDigitCount() {
		assertEquals(1, PictureUtils.digitCount("9"));
		assertEquals(2, PictureUtils.digitCount("99"));
		assertEquals(3, PictureUtils.digitCount("999"));
		assertEquals(1, PictureUtils.digitCount("9(1)"));
		assertEquals(2, PictureUtils.digitCount("9(2)"));
		assertEquals(3, PictureUtils.digitCount("9(3)"));
		assertEquals(18, PictureUtils.digitCount("9(18)"));
	}

	@Test
	public void testNotCompNumeric() {
		try {
			PictureUtils.compNumeric("X(5)").toString();
			fail();
		} catch (Exception e) {
			assertEquals("Picture clause X(5) is not a computational picture", e.getMessage());
		}
	}

	@Test
	public void testAlphaNumeric() {
		assertEquals("AlphaNumeric[charNum=1]", PictureUtils.alphaNumeric("X").toString());
		assertEquals("AlphaNumeric[charNum=15]", PictureUtils.alphaNumeric("X(15)").toString());
		assertEquals("AlphaNumeric[charNum=9]", PictureUtils.alphaNumeric("+9(5).99").toString());
		assertEquals("AlphaNumeric[charNum=10]", PictureUtils.alphaNumeric("$ZZ,ZZ9.99").toString());
		assertEquals("AlphaNumeric[charNum=15]", PictureUtils.alphaNumeric("-9V9(9)E-99").toString());
		assertEquals("AlphaNumeric[charNum=23]", PictureUtils.alphaNumeric("$$ABEGNPSVXZ90/,.+-CRDB*").toString());
		assertEquals("AlphaNumeric[charNum=30]", PictureUtils.alphaNumeric("z(2)z9/z(23)DB").toString());
	}

	@Test
	public void testCharCount() {
		assertEquals(1, PictureUtils.charCount("X"));
		assertEquals(2, PictureUtils.charCount("XX"));
		assertEquals(3, PictureUtils.charCount("XXX"));
		assertEquals(1, PictureUtils.charCount("X(1)"));
		assertEquals(2, PictureUtils.charCount("X(2)"));
		assertEquals(3, PictureUtils.charCount("X(3)"));
		assertEquals(1, PictureUtils.charCount("A"));
		assertEquals(2, PictureUtils.charCount("AA"));
		assertEquals(3, PictureUtils.charCount("AAA"));
		assertEquals(1, PictureUtils.charCount("A(1)"));
		assertEquals(2, PictureUtils.charCount("A(2)"));
		assertEquals(3, PictureUtils.charCount("A(3)"));
		assertEquals(1, PictureUtils.charCount("9"));
		assertEquals(2, PictureUtils.charCount("99"));
		assertEquals(3, PictureUtils.charCount("999"));
		assertEquals(1, PictureUtils.charCount("9(1)"));
		assertEquals(2, PictureUtils.charCount("9(2)"));
		assertEquals(3, PictureUtils.charCount("9(3)"));
		
	}
}
