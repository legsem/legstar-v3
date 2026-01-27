package org.legstar.cobol.generator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.data.entry.CobolDataEntryUsage;

public class RenderingModelGeneratorTest {

	@Test
	public void testString() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("A(18)") //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals("RenderingString[cobolName=MYVAR, charNum=18, array=null, fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testBinaryNumber() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S99V9") //
				.usage(CobolDataEntryUsage.BINARY) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingBinaryNumber[cobolName=MYVAR, signed=true, totalDigits=3, odoObject=false, array=null, fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testZonedDecimal() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S9(5)V99") //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingZonedDecimal[cobolName=MYVAR, totalDigits=7, fractionDigits=2, signLeading=false, signSeparate=false, odoObject=false, array=null, fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testPackedDecimal() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S9(5)V99") //
				.usage(CobolDataEntryUsage.PACKED_DECIMAL) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingPackedDecimal[cobolName=MYVAR, signed=true, totalDigits=7, fractionDigits=2, odoObject=false, array=null, fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testStringArray() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("A(18)") //
				.minOccurs(5) //
				.maxOccurs(5) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingString[cobolName=MYVAR, charNum=18, array=RenderingArray[minOccurs=5, maxOccurs=5, dependingOn=null], fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testBinaryNumberArray() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S99V9") //
				.usage(CobolDataEntryUsage.BINARY) //
				.minOccurs(5) //
				.maxOccurs(5) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingBinaryNumber[cobolName=MYVAR, signed=true, totalDigits=3, odoObject=false, array=RenderingArray[minOccurs=5, maxOccurs=5, dependingOn=null], fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testZonedDecimalArray() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S9(5)V99") //
				.minOccurs(5) //
				.maxOccurs(5) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingZonedDecimal[cobolName=MYVAR, totalDigits=7, fractionDigits=2, signLeading=false, signSeparate=false, odoObject=false, array=RenderingArray[minOccurs=5, maxOccurs=5, dependingOn=null], fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testPackedDecimalArray() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S9(5)V99") //
				.usage(CobolDataEntryUsage.PACKED_DECIMAL) //
				.minOccurs(5) //
				.maxOccurs(5) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingPackedDecimal[cobolName=MYVAR, signed=true, totalDigits=7, fractionDigits=2, odoObject=false, array=RenderingArray[minOccurs=5, maxOccurs=5, dependingOn=null], fieldName=myvar]",
				model.cobol_item().toString());
	}

	@Test
	public void testGroup() {
		CobolDataEntry child = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("CHILD") //
				.picture("S9(5)V99") //
				.build();
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("GROUP") //
				.addChild(child) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingGroup[cobolName=GROUP, fields=[RenderingZonedDecimal[cobolName=CHILD, totalDigits=7, fractionDigits=2, signLeading=false, signSeparate=false, odoObject=false, array=null, fieldName=child]], array=null, fieldName=group]",
				model.cobol_item().toString());
	}

	@Test
	public void testGroupArray() {
		CobolDataEntry child = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("CHILD") //
				.picture("S9(5)V99") //
				.build();
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("GROUP") //
				.addChild(child) //
				.minOccurs(5) //
				.maxOccurs(5) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingGroup[cobolName=GROUP, fields=[RenderingZonedDecimal[cobolName=CHILD, totalDigits=7, fractionDigits=2, signLeading=false, signSeparate=false, odoObject=false, array=null, fieldName=child]], array=RenderingArray[minOccurs=5, maxOccurs=5, dependingOn=null], fieldName=group]",
				model.cobol_item().toString());
	}

	@Test
	public void testVariableSizeArray() {
		CobolDataEntry child1 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("COUNTER") //
				.picture("9(4)") //
				.build();
		CobolDataEntry child2 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("ARRAY") //
				.picture("X(1") //
				.minOccurs(0) //
				.maxOccurs(5) //
				.dependingOn("COUNTER") //
				.build();
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("GROUP") //
				.addChild(child1) //
				.addChild(child2) //
				.build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals("RenderingGroup[cobolName=GROUP, fields=["
				+ "RenderingZonedDecimal[cobolName=COUNTER, totalDigits=4, fractionDigits=0, signLeading=false, signSeparate=false, odoObject=true, array=null, fieldName=counter], "
				+ "RenderingString[cobolName=ARRAY, charNum=3, array=RenderingArray[minOccurs=0, maxOccurs=5, dependingOn=COUNTER], fieldName=array]"
				+ "], array=null, fieldName=group]", model.cobol_item().toString());
	}

	@Test
	public void testChoicePrimitives() {
		CobolDataEntry alt1 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("COM-NAME") //
				.picture("X(10)") //
				.build();
		CobolDataEntry alt2 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("COM-AMOUNT") //
				.picture("9(5)V99") //
				.usage(CobolDataEntryUsage.PACKED_DECIMAL) //
				.redefines("COM-NAME") //
				.build();
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("GROUP") //
				.addChild(alt1) //
				.addChild(alt2) //
				.build();

		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals("RenderingGroup[cobolName=GROUP, fields=[" + "RenderingChoice[alternatives=["
				+ "RenderingString[cobolName=COM-NAME, charNum=10, array=null, fieldName=comName], "
				+ "RenderingPackedDecimal[cobolName=COM-AMOUNT, signed=false, totalDigits=7, fractionDigits=2, odoObject=false, array=null, fieldName=comAmount]"
				+ "], array=null, fieldName=comNameChoice]" + "], array=null, fieldName=group]",
				model.cobol_item().toString());
	}

	@Test
	public void testFieldNameCollision() {
		CobolDataEntry child1 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("COM-NAME") //
				.picture("X(10)") //
				.build();
		CobolDataEntry child2 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("FILLER") //
				.picture("X(10)") //
				.build();
		CobolDataEntry child3 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("FILLER") //
				.picture("X(10)") //
				.build();
		CobolDataEntry child4 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("COM-ADDRESS") //
				.picture("X(10)") //
				.build();
		CobolDataEntry child5 = new CobolDataEntry.Builder() //
				.levelNumber(5) //
				.cobolName("FILLER") //
				.picture("X(10)") //
				.build();
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("GROUP") //
				.addChild(child1) //
				.addChild(child2) //
				.addChild(child3) //
				.addChild(child4) //
				.addChild(child5) //
				.build();

		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals("RenderingGroup[cobolName=GROUP, fields=["
				+ "RenderingString[cobolName=COM-NAME, charNum=10, array=null, fieldName=comName], "
				+ "RenderingString[cobolName=FILLER, charNum=10, array=null, fieldName=filler], "
				+ "RenderingString[cobolName=FILLER, charNum=10, array=null, fieldName=filler_2], "
				+ "RenderingString[cobolName=COM-ADDRESS, charNum=10, array=null, fieldName=comAddress], "
				+ "RenderingString[cobolName=FILLER, charNum=10, array=null, fieldName=filler_4]"
				+ "], array=null, fieldName=group]",
				model.cobol_item().toString());
	}

}
