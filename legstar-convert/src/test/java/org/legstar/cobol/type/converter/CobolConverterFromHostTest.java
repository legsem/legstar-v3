package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import legstar.samples.flat01.Flat01Record;

public class CobolConverterFromHostTest extends CobolConverterTestBase {

	private static final ObjectMapper OBJECT_MAPPER_SINGLETON = new ObjectMapper();

	@Test
	public void testFlat01() throws JsonProcessingException {
		CobolConverterFromHost<Flat01Record> fromHost = new CobolConverterFromHost<>(config);
		Flat01Record output = fromHost.convert(
				inputStreamFrom("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F"), Flat01Record.class);
		assertEquals("", OBJECT_MAPPER_SINGLETON.writeValueAsString(output));
	}

}
