package org.legstar.cobol.jaxb.converter;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.converter.CobolConverterFromHostChoiceStrategy;
import org.legstar.cobol.converter.CobolConverterTestBase;

import legstar.samples.jaxb.flat01.Flat01Record;
import legstar.samples.jaxb.custdat.CustomerData;
import legstar.samples.jaxb.alltypes.Alltypes;

public class CobolJaxbConverterFromHostTest extends CobolConverterTestBase {

	@Test
	public void testAlltypes() {
		check(convert(
				"C1C2C3C4000000008000007f800000000000ffff7fffffffffffffff00000003ffffffff001234567D403733c6404cbfcdcafd37c0F1F1F1F0C0F2F1F2F0F0D3F3F3F4D44EF5F6F6604040404040",
				Alltypes.class));
	}

	@Test
	public void testCustomerData() {
		check(convert(
				"F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C",
				CustomerData.class));
	}

	@Test
	public void testFlat01() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class));
	}

	private <T> String convert(String payload, Class<T> clazz) {
		return convert(payload, clazz, null);
	}

	private <T> String convert(String payload, Class<T> clazz, CobolConverterFromHostChoiceStrategy<T> choiceStrategy) {
		try {
			CobolJaxbConverterConfig config = CobolJaxbConverterConfig.ebcdic();
			CobolJaxbConverterFromHost<T> fromHost = choiceStrategy == null ? new CobolJaxbConverterFromHost<>(config)
					: new CobolJaxbConverterFromHost<>(config, choiceStrategy);
			StringWriter writer = new StringWriter();
			fromHost.convert(inputStreamFrom(payload), clazz, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
