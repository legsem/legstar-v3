package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import legstar.samples.custdat.CustomerData;
import legstar.samples.flat01.Flat01Record;

public class CobolBeanConverterStreamTest extends CobolConverterTestBase {

	@Test
	public void testMultipleBeans() throws IOException {
		Path filePath = Paths.get("src/test/data/ZOS.FCUSTDAT.RDW.bin");
		FileInputStream fis = new FileInputStream(filePath.toFile());
		CobolInputStream cis = new CobolInputStream(fis, CobolRecordFormat.V);
		CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
		CustomerData last = converter.convertAll(cis).reduce((a, b) -> b).orElse(null);
		check(Objects.toString(last));
	}

	@Test
	public void testEmptyStream() throws IOException {
		CobolInputStream cis = new CobolInputStream(inputStreamFrom(""));
		CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
		assertEquals(0, converter.convertAll(cis).count());
	}

	@Test
	public void testSingleBean() throws IOException {
		CobolInputStream cis = new CobolInputStream(inputStreamFrom("F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C"));
		CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
		assertEquals(1, converter.convertAll(cis).count());
	}

	@Test
	public void testConversionError() throws IOException {
		CobolInputStream cis = new CobolInputStream(inputStreamFrom("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000A"));
		CobolBeanConverter<Flat01Record> converter = new CobolBeanConverter<>(Flat01Record.class);
		try {
			converter.convertAll(cis).count();
			fail();
		} catch (Exception e) {
			assertEquals("Low nibble 10 at byte position 3 is invalid for a BigDecimal. Not a sign indicator. {Cobol item: 'FLAT01-RECORD.COM-AMOUNT', @offset: 30}", e.getMessage());
		}
	}

}
