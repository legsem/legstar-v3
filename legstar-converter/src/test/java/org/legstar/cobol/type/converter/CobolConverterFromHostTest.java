package org.legstar.cobol.type.converter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import legstar.samples.ardo01.Ardo01Record;
import legstar.samples.ardo03.Ardo03Record;
import legstar.samples.ardo04.Ardo04Record;
import legstar.samples.custdat.CustomerData;
import legstar.samples.flat01.Flat01Record;
import legstar.samples.flat02.Flat02Record;
import legstar.samples.optl01.Optl01Record;
import legstar.samples.rdef01.Rdef01Record;
import legstar.samples.rdef03.Rdef03Record;
import legstar.samples.rdef03.Rdef03RecordChoiceStrategy;
import legstar.samples.stru01.Stru01Record;
import legstar.samples.stru03.Stru03Record;

/**
 * TODO
 * <ul>
 * <li>Add case choice with an array alternative</li>
 * <li>Add case array with Choice items</li>
 * <li>Add case primitive as root</li>
 * <li>Add case choice as root</li>
 * <li>Add case array as root</li>
 * </ul>
 */
public class CobolConverterFromHostTest extends CobolConverterTestBase {
	
	private static final ObjectMapper OBJECT_MAPPER_SINGLETON = JsonMapper.builder()
			.configure(SerializationFeature.INDENT_OUTPUT, true)
			.defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
			.build();

	@Test
	public void testFlat01() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class));
	}

	@Test
	public void testFlat02() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F00010002000300040005", Flat02Record.class));
	}

	@Test
	public void testStru01() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F003EC1C2", Stru01Record.class));
	}

	@Test
	public void testStru03() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F0001C1C20002C2C30003C3C40004C4C50005C5C6", Stru03Record.class));
	}

	@Test
	public void testArdo01EmptyArray() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400000", Ardo01Record.class));
	}

	@Test
	public void testArdo01OneItem() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400001000000000023556C", Ardo01Record.class));
	}

	@Test
	public void testArdo01Full() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400005000000000023556C000000000023656C000000000023756C000000000023856C000000000023956C", Ardo01Record.class));
	}

	@Test
	public void testArdo03NoItemsAtAll() {
		check(convert("F0F0F0F0F0", Ardo03Record.class));
	}

	@Test
	public void testArdo03OneOuterNoInners() {
		check(convert("F0F0F0F0F1F0F0F0", Ardo03Record.class));
	}

	@Test
	public void testArdo03OneOuterOneInner() {
		check(convert("F0F0F0F0F1F0F0F1C1C2C3C4", Ardo03Record.class));
	}

	@Test
	public void testArdo03TwoOutersTwoInnersOneInner() {
		check(convert("F0F0F0F0F2F0F0F2C1C2C3C4C5C6C7C8F0F0F1C9D1D2D3D4", Ardo03Record.class));
	}

	@Test
	public void testArdo04() {
		check(convert("0003C1404040400001C2404040400002C3404040400003", Ardo04Record.class));
	}

	@Test
	public void testOptl01None() {
		check(convert("F0F0F0F0F0F0", Optl01Record.class));
	}

	@Test
	public void testOptl01First() {
		check(convert("F0F0F1F0F0F0C3C1D4C2D90001", Optl01Record.class));
	}

	@Test
	public void testCustomerData() {
		check(convert(
				"F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C",
				CustomerData.class));
	}

	@Test
	public void testRdef01Choice1() {
		check(convert("0001D1D6C8D540E2D4C9E3C8", Rdef01Record.class));
	}

	@Test
	public void testRdef03Choice1() {
		check(convert("0000C3C1D4C2D9C9C4C7C540", Rdef03Record.class, new Rdef03RecordChoiceStrategy()));
	}

	@Test
	public void testRdef03Choice2() {
		check(convert("00010250000F", Rdef03Record.class, new Rdef03RecordChoiceStrategy()));
	}

	@Test
	public void testRdef03Choice3() {
		check(convert("0002F1F2F3F4F5", Rdef03Record.class, new Rdef03RecordChoiceStrategy()));
	}

	private <T> String convert(String payload, Class<T> clazz) {
		return convert(payload, clazz, null);
	}

	private <T> String convert(String payload, Class<T> clazz, CobolConverterFromHostChoiceStrategy<T> choiceStrategy) {
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
