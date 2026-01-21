package org.legstar.cobol.generator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.data.entry.CobolDataEntryUsage;

public class RenderingModelGeneratorTest {

	@Test
	public void testBinaryNumber() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S99V9") //
				.usage(CobolDataEntryUsage.BINARY).build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals("RenderingBinaryNumber[cobolName=MYVAR, signed=true, totalDigits=3, odoObject=false]",
				model.cobol_item().toString());
	}

	@Test
	public void testZonedDecimal() {
		CobolDataEntry entry = new CobolDataEntry.Builder() //
				.levelNumber(1) //
				.cobolName("MYVAR") //
				.picture("S9(5)V99").build();
		RenderingModelGenerator generator = new RenderingModelGenerator();
		RenderingModel model = generator.generate("t", entry, "some.package");
		assertEquals(
				"RenderingZonedDecimal[cobolName=MYVAR, totalDigits=7, fractionDigits=2, signLeading=false, signSeparate=false, odoObject=false]",
				model.cobol_item().toString());
	}
}
