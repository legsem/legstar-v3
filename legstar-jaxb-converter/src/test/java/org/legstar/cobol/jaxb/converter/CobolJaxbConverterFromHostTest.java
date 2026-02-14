package org.legstar.cobol.jaxb.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringWriter;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolConverterTestBase;

import legstar.samples.jaxb.flat01.Flat01Record;
import legstar.samples.jaxb.custdat.CustomerData;
import legstar.samples.jaxb.ardo01.Ardo01Record;
import legstar.samples.jaxb.ardo03.Ardo03Record;
import legstar.samples.jaxb.ardo04.Ardo04Record;
import legstar.samples.jaxb.digitname._5500Rec01;
import legstar.samples.jaxb.flat02.Flat02Record;
import legstar.samples.jaxb.freeform.RecA;
import legstar.samples.jaxb.optl01.Optl01Record;
import legstar.samples.jaxb.rdef01.Rdef01Record;
import legstar.samples.jaxb.rdef02.Rdef02Record;
import legstar.samples.jaxb.rdef03.Rdef03Record;
import legstar.samples.jaxb.rdef03.Rdef03RecordChoiceStrategy;
import legstar.samples.jaxb.rdef04.Rdef04Record;
import legstar.samples.jaxb.rdef04.Rdef04RecordChoiceStrategy;
import legstar.samples.jaxb.rdef05.Rdef05Record;
import legstar.samples.jaxb.rdef05.Rdef05RecordChoiceStrategy;
import legstar.samples.jaxb.rdef06.Rdef06Record;
import legstar.samples.jaxb.rdef06.Rdef06RecordChoiceStrategy;
import legstar.samples.jaxb.rdef07.Rdef07Record;
import legstar.samples.jaxb.stru01.Stru01Record;
import legstar.samples.jaxb.stru03.Stru03Record;
import legstar.samples.jaxb.alltypes.Alltypes;

public class CobolJaxbConverterFromHostTest extends CobolConverterTestBase {

	@Test
	public void testAlltypes() {
		check(convert(
				"C1C2C3C4000000008000007f800000000000ffff7fffffffffffffff00000003ffffffff001234567D403733c6404cbfcdcafd37c0F1F1F1F0C0F2F1F2F0F0D3F3F3F4D44EF5F6F6604040404040",
				Alltypes.class));
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
		check(convert(
				"F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400005000000000023556C000000000023656C000000000023756C000000000023856C000000000023956C",
				Ardo01Record.class));
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
	public void testCustomerData() {
		check(convert(
				"F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C",
				CustomerData.class));
	}

	@Test
	public void test_5500Rec01() {
		check(convert("F5C3C1D4C2D9C9", _5500Rec01.class));
	}

	@Test
	public void testFlat01() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class));
	}
	
	@Test
	public void testFlat01Formatted() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F", Flat01Record.class, null, true));
	}


	@Test
	public void testFlat02() {
		check(convert("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F00010002000300040005",
				Flat02Record.class));
	}

	@Test
	public void testRecA() {
		check(convert(
				"D7D7D7D7F0F0C3C1D4C2D9C9C4C7F1F1F1F6F7F8F9F1F2C1F5F4F3F2F1C2C2C3C4404040404040404040404040404040404040404040404040404040404040404040C4C5",
				RecA.class));
	}

	@Test
	public void testOptl01None() {
		check(convert("F0F0F0F0F0F0", Optl01Record.class));
	}

	@Test
	public void testOptl01StructPresentStringAbsent() {
		check(convert("F0F0F1F0F0F0F1F2F3F4F5F6F7F8F9F0F1F2F3F4F5F6F7F8C1C2C3C4C5", Optl01Record.class));
	}

	@Test
	public void testRdef01Choice1() {
		check(convert("0001D1D6C8D540E2D4C9E3C8", Rdef01Record.class));
	}

	@Test
	public void testRdef02Choice1() {
		check(convert("D1D6C8D540E20001C3C1D4C2D9C9C4C7C5400250000F", Rdef02Record.class));
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

	@Test
	public void testRdef04Choice1() {
		check(convert("C3C1D4C2D9C9C4C7C540C1", Rdef04Record.class));
	}

	@Test
	public void testRdef04Choice2() {
		check(convert("C3C1D4C2D9C9C4C7C540C1", Rdef04Record.class, new Rdef04RecordChoiceStrategy()));
	}

	@Test
	public void testRdef04NoChoice() {
		try {
			check(convert("C3C1D4C2D9C9C4C7C540C1", Rdef04Record.class, new CobolChoiceStrategy<Rdef04Record>() {

				@Override
				public boolean choose(Rdef04Record root, Object choice, Field alternative) {
					return false;
				}

			}));
			fail();
		} catch (Exception e) {
			assertEquals("org.legstar.cobol.converter.CobolBeanConverterException:"
					+ " None of the 2 alternatives matched the data"
					+ " {Cobol item: 'RDEF04-RECORD.OUTER-REDEFINES-LONG', @offset: 0}", e.getMessage());
		}
	}

	@Test
	public void testRdef05Choice1() {
		check(convert("F1F2F3F4F5F6F7F8", Rdef05Record.class));
	}

	@Test
	public void testRdef05Choice2() {
		check(convert("F1F2F3F4F5F6F7F8", Rdef05Record.class, new Rdef05RecordChoiceStrategy()));
	}

	@Test
	public void testRdef06Choice1() {
		check(convert("F0F0F1C3C1D4C2D9C9C4C7C540C3C1D4C2D9C9C4C7C540404040", Rdef06Record.class));
	}

	@Test
	public void testRdef06Choice2() {
		check(convert("F0F0F1C3C1D4C2D9C9C4C7C540C3C1D4C2D9C9C4C7C540404040", Rdef06Record.class,
				new Rdef06RecordChoiceStrategy()));
	}

	@Test
	public void testRdef07Choice() {
		check(convert("F0F0F2052FC1C2C3", Rdef07Record.class));
	}

	@Test
	public void testStru01() {
		check(convert("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F003EC1C2", Stru01Record.class));
	}

	@Test
	public void testStru03() {
		check(convert(
				"F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F0001C1C20002C2C30003C3C40004C4C50005C5C6",
				Stru03Record.class));
	}

	private <T> String convert(String payload, Class<T> beanClass) {
		return convert(payload, beanClass, null);
	}

	private <T> String convert(String payload, Class<T> beanClass, CobolChoiceStrategy<T> choiceStrategy) {
		return convert(payload, beanClass, choiceStrategy, false);
	}

	private <T> String convert(String payload, Class<T> beanClass, CobolChoiceStrategy<T> choiceStrategy,
			boolean formattedOutput) {
		try {
			CobolXmlConverterConfig config = CobolXmlConverterConfig.ebcdic() //
					.setFormattedOutput(formattedOutput);
			CobolXmlConverter<T> fromHost = choiceStrategy == null //
					? new CobolXmlConverter<>(config, beanClass) //
					: new CobolXmlConverter<>(config, beanClass, choiceStrategy);
			StringWriter writer = new StringWriter();
			fromHost.convert(inputStreamFrom(payload), writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
