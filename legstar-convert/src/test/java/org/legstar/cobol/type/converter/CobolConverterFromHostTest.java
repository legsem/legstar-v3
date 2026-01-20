package org.legstar.cobol.type.converter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import legstar.samples.custdat.CustomerData;
import legstar.samples.flat01.Flat01Record;
import legstar.samples.rdef03.Rdef03ConverterChoiceStrategy;
import legstar.samples.rdef03.Rdef03Record;

public class CobolConverterFromHostTest extends CobolConverterTestBase {

	private static final ObjectMapper OBJECT_MAPPER_SINGLETON = new ObjectMapper()
			.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
			.enable(SerializationFeature.INDENT_OUTPUT);;

	@Test
	public void testFlat01() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class));
	}

	@Test
	public void testCustomerData() {
		check(convert(
				"F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C",
				CustomerData.class));
	}

	@Test
	public void testRdef03() {
		check(convert(
				"0002F1F2F3F4F50000000000",
				Rdef03Record.class, new Rdef03ConverterChoiceStrategy()));
	}

	private <T> String convert(String payload, Class<T> clazz) {
		return convert(payload, clazz, null);
	}
	
	private <T> String convert(String payload, Class<T> clazz, CobolConverterFromHostChoiceStrategy choiceStrategy) {
		try {
			CobolConverterFromHost<T> fromHost = choiceStrategy == null ? new CobolConverterFromHost<>(config)
					: new CobolConverterFromHost<>(config, choiceStrategy);
			T output = fromHost.convert(inputStreamFrom(payload), clazz);
			return OBJECT_MAPPER_SINGLETON.writeValueAsString(output);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
