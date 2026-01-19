package org.legstar.cobol.type.converter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import legstar.samples.flat01.Flat01Record;

public class CobolConverterFromHostTest extends CobolConverterTestBase {

	private static final ObjectMapper OBJECT_MAPPER_SINGLETON = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

	@Test
	public void testFlat01() throws JsonProcessingException {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class));
	}
	
	private <T> String convert(String payload, Class<T> clazz) {
		try {
			CobolConverterFromHost<T> fromHost = new CobolConverterFromHost<>(config);
			T output = fromHost.convert(
					inputStreamFrom(payload), clazz);
			return OBJECT_MAPPER_SINGLETON.writeValueAsString(output);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
