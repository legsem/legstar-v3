package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;
import org.legstar.cobol.io.CobolOutputStream;

import legstar.samples.alltypes.Alltypes;
import legstar.samples.ardo01.Ardo01Record;
import legstar.samples.ardo04.Ardo04Record;
import legstar.samples.custdat.CustomerData;
import legstar.samples.flat01.Flat01Record;
import legstar.samples.flat02.Flat02Record;
import legstar.samples.optl01.Optl01Record;
import legstar.samples.rdef01.Rdef01Record;
import legstar.samples.rdef02.Rdef02Record;
import legstar.samples.rdef03.Rdef03Record;
import legstar.samples.rdef04.Rdef04Record;
import legstar.samples.rdef06.Rdef06Record;
import legstar.samples.rdef07.Rdef07Record;
import legstar.samples.stru01.Stru01Record;
import legstar.samples.stru03.Stru03Record;

public class CobolBeanSerializerTest extends CobolConverterTestBase {

	@Test
	public void testFlat01() {
		Flat01Record bean = new Flat01Record();
		bean.setComName("Antonio Bustello");
		bean.setComNumber(new BigDecimal(8956));
		bean.setComAmount(new BigDecimal("235.56"));
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f", serialize(bean));
	}

	@Test
	public void testFlat01Empty() {
		Flat01Record bean = new Flat01Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000000f", serialize(bean));
	}

	@Test
	public void testFlat02() {
		Flat02Record bean = new Flat02Record();
		bean.setComName("Antonio Bustello");
		bean.setComNumber(new BigDecimal(8956));
		bean.setComAmount(new BigDecimal("235.56"));
		bean.setComArray(new Short[] { 5, 6, 7, 8, 9 });
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f00050006000700080009",
				serialize(bean));
	}

	@Test
	public void testFlat02Empty() {
		Flat02Record bean = new Flat02Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000000f00000000000000000000",
				serialize(bean));
	}

	@Test
	public void testStru01() {
		Stru01Record bean = new Stru01Record();
		bean.setComName("Antonio Bustello");
		bean.setComNumber(new BigDecimal(8956));
		bean.setComAmount(new BigDecimal("235.56"));
		Stru01Record.ComSubRecord subRecord = new Stru01Record.ComSubRecord();
		subRecord.setComItem1((short) 279);
		subRecord.setComItem2("JU");
		bean.setComSubRecord(subRecord);
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f0117d1e4", serialize(bean));
	}

	@Test
	public void testStru01Empty() {
		Stru01Record bean = new Stru01Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000000f00004040", serialize(bean));
	}

	@Test
	public void testStru03() {
		Stru03Record bean = new Stru03Record();
		bean.setComName("Antonio Bustello");
		bean.setComNumber(new BigDecimal(8956));
		bean.setComAmount(new BigDecimal("235.56"));
		Stru03Record.ComArray subItem1 = new Stru03Record.ComArray();
		subItem1.setComItem1((short) 279);
		subItem1.setComItem2("JU");
		Stru03Record.ComArray subItem2 = new Stru03Record.ComArray();
		subItem2.setComItem1((short) 280);
		subItem2.setComItem2("JV");
		Stru03Record.ComArray subItem3 = new Stru03Record.ComArray();
		subItem3.setComItem1((short) 281);
		subItem3.setComItem2("JW");
		Stru03Record.ComArray subItem4 = new Stru03Record.ComArray();
		subItem4.setComItem1((short) 282);
		subItem4.setComItem2("JX");
		Stru03Record.ComArray subItem5 = new Stru03Record.ComArray();
		subItem5.setComItem1((short) 283);
		subItem5.setComItem2("JY");
		bean.setComArray(new Stru03Record.ComArray[] { subItem1, subItem2, subItem3, subItem4, subItem5 });
		assertEquals(
				"f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f0117d1e40118d1e50119d1e6011ad1e7011bd1e8",
				serialize(bean));
	}

	@Test
	public void testStru03Empty() {
		Stru03Record bean = new Stru03Record();
		assertEquals(
				"f0f0f0f0f0f040404040404040404040404040404040404040400000000f0000404000004040000040400000404000004040",
				serialize(bean));
	}

	@Test
	public void testAlltypes() {
		Alltypes bean = new Alltypes();
		bean.setS_String("Carl");
		bean.setS_Binary("Bin");
		bean.setS_Short((short) 255);
		bean.setS_Ushort(Short.MAX_VALUE);
		bean.setS_Int(-89357);
		bean.setS_Uint(Integer.MAX_VALUE);
		bean.setS_Long(56894356891256l);
		bean.setS_Ulong(Long.MAX_VALUE);
		bean.setS_Pdecimal(new BigDecimal("265.21"));
		bean.setS_Float(568E12f);
		bean.setS_Double(562389E-78d);
		bean.setS_Zoned(new BigDecimal("-256.56"));
		bean.setS_Uzoned(new BigDecimal("256.56"));
		bean.setS_ZonedSl(new BigDecimal("-256.56"));
		bean.setS_ZonedSt(new BigDecimal("-256.56"));
		bean.setS_ZonedSls(new BigDecimal("-256.56"));
		bean.setS_ZonedSts(new BigDecimal("-256.56"));
		bean.setS_UzonedBwz(new BigDecimal("0"));
		assertEquals(
				"c3819993c289954000ff7ffffffea2f37fffffff000033bebfeea6787fffffffffffffff000026521f4d20497d04fe6032808def80f2f5f6f5d6f2f5f6f5f6d2f5f6f5d660f6f5f6604040404040",
				serialize(bean));
	}

	@Test
	public void testAlltypesEmpty() {
		Alltypes bean = new Alltypes();
		assertEquals(
				"404040404040404000000000000000000000000000000000000000000000000000000000000000000f000000000000000000000000f0f0f0f0c0f0f0f0f0f0c0f0f0f0c04ef0f0f04e4040404040",
				serialize(bean));
	}

	@Test
	public void testArdo01() {
		Ardo01Record bean = new Ardo01Record();
		bean.setComName("Antonio Bustello");
		bean.setComNumber(new BigDecimal(8956));
		bean.setComNbr((short) 2);
		bean.setComArray(new BigDecimal[] { new BigDecimal("265.23"), new BigDecimal("-36.45") });
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400002000000000026523c000000000003645d",
				serialize(bean));
	}

	@Test
	public void testArdo01Empty() {
		Ardo01Record bean = new Ardo01Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000", serialize(bean));
	}

	@Test
	public void testArdo04() {
		Ardo04Record bean = new Ardo04Record();
		bean.setC_ItemsNumber((short) 3);
		Ardo04Record.C_Array i1 = new Ardo04Record.C_Array();
		i1.setC_Item1("I1");
		i1.setC_Item2((short) 1);
		Ardo04Record.C_Array i2 = new Ardo04Record.C_Array();
		i2.setC_Item1("I2");
		i2.setC_Item2((short) 2);
		Ardo04Record.C_Array i3 = new Ardo04Record.C_Array();
		i3.setC_Item1("I3");
		i3.setC_Item2((short) 3);
		bean.setC_Array(new Ardo04Record.C_Array[] { i1, i2, i3 });
		assertEquals("0003c9f14040400001c9f24040400002c9f34040400003", serialize(bean));
	}

	@Test
	public void testArdo04Empty() {
		Ardo04Record bean = new Ardo04Record();
		assertEquals("000040404040400000", serialize(bean));
	}

	@Test
	public void testCustomerData() {
		CustomerData bean = new CustomerData();
		bean.setCustomerId(new BigDecimal(72));
		assertEquals(
				"f0f0f0f0f7f240404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404000000000",
				serialize(bean));
	}

	@Test
	public void testOptl01None() {
		Optl01Record bean = new Optl01Record();
		bean.setOptlItemInd(BigDecimal.ZERO);
		bean.setOptlStructInd(BigDecimal.ZERO);
		assertEquals("f0f0f0f0f0f0", serialize(bean));
	}

	@Test
	public void testOptl01Item() {
		Optl01Record bean = new Optl01Record();
		bean.setOptlItemInd(BigDecimal.valueOf(1));
		bean.setOptlStructInd(BigDecimal.ZERO);
		bean.setOptlItem(new String[] { "Galileo di Vincenzo Bonaiuti de Galilei" });
		assertEquals("f0f0f0f0f0f1c781938993859640848940e58995838595a99640c296958189a4a38940848540", serialize(bean));
	}

	@Test
	public void testOptl01Struct() {
		Optl01Record bean = new Optl01Record();
		bean.setOptlItemInd(BigDecimal.ZERO);
		bean.setOptlStructInd(BigDecimal.valueOf(1));
		Optl01Record.OptlStruct struct = new Optl01Record.OptlStruct();
		struct.setOptlStructField1("F1");
		struct.setOptlStructField2("F2");
		bean.setOptlStruct(new Optl01Record.OptlStruct[] { struct });
		assertEquals("f0f0f1f0f0f0c6f140404040404040404040404040404040c6f2404040", serialize(bean));
	}

	@Test
	public void testOptl01Both() {
		Optl01Record bean = new Optl01Record();
		bean.setOptlItemInd(BigDecimal.valueOf(1));
		bean.setOptlItem(new String[] { "Galileo di Vincenzo Bonaiuti de Galilei" });
		bean.setOptlStructInd(BigDecimal.valueOf(1));
		Optl01Record.OptlStruct struct = new Optl01Record.OptlStruct();
		struct.setOptlStructField1("F1");
		struct.setOptlStructField2("F2");
		bean.setOptlStruct(new Optl01Record.OptlStruct[] { struct });
		assertEquals(
				"f0f0f1f0f0f1c6f140404040404040404040404040404040c6f2404040c781938993859640848940e58995838595a99640c296958189a4a38940848540",
				serialize(bean));
	}

	@Test
	public void testRdef01Alt1() {
		Rdef01Record bean = new Rdef01Record();
		bean.setComSelect((short) 0);
		Rdef01Record.ComDetail1Choice choice = new Rdef01Record.ComDetail1Choice();
		Rdef01Record.ComDetail1Choice.ComDetail1 alt1 = new Rdef01Record.ComDetail1Choice.ComDetail1();
		alt1.setComName("Hadock");
		choice.setComDetail1(alt1);
		bean.setComDetail1Choice(choice);
		assertEquals("0000c8818496839240404040", serialize(bean));
	}

	@Test
	public void testRdef01Alt2() {
		Rdef01Record bean = new Rdef01Record();
		bean.setComSelect((short) 1);
		Rdef01Record.ComDetail1Choice choice = new Rdef01Record.ComDetail1Choice();
		Rdef01Record.ComDetail1Choice.ComDetail2 alt2 = new Rdef01Record.ComDetail1Choice.ComDetail2();
		alt2.setComAmount(new BigDecimal("265.24"));
		choice.setComDetail2(alt2);
		bean.setComDetail1Choice(choice);
		assertEquals("00010026524f", serialize(bean));
	}

	@Test
	public void testRdef02Alt1() {
		Rdef02Record bean = new Rdef02Record();
		Rdef02Record.Rdef02Key key = new Rdef02Record.Rdef02Key();
		Rdef02Record.Rdef02Key.Rdef02Item1Choice keyChoice = new Rdef02Record.Rdef02Key.Rdef02Item1Choice();
		keyChoice.setRdef02Item1(new BigDecimal("-5689236"));
		key.setRdef02Item1Choice(keyChoice);
		key.setComSelect((short) 0);
		bean.setRdef02Key(key);
		Rdef02Record.ComDetail1Choice choice = new Rdef02Record.ComDetail1Choice();
		Rdef02Record.ComDetail1Choice.ComDetail1 alt1 = new Rdef02Record.ComDetail1Choice.ComDetail1();
		alt1.setComName("Hadock");
		choice.setComDetail1(alt1);
		bean.setComDetail1Choice(choice);
		bean.setRdef02Key(key);
		bean.setComItem3(new BigDecimal("568.56"));
		assertEquals("00005689236d0000c88184968392404040400056856f", serialize(bean));
	}

	@Test
	public void testRdef03Alt1() {
		Rdef03Record bean = new Rdef03Record();
		bean.setComSelect((short) 0);
		Rdef03Record.ComDetail1Choice choice = new Rdef03Record.ComDetail1Choice();
		Rdef03Record.ComDetail1Choice.ComDetail1 alt1 = new Rdef03Record.ComDetail1Choice.ComDetail1();
		alt1.setComName("Hadock");
		choice.setComDetail1(alt1);
		bean.setComDetail1Choice(choice);
		assertEquals("0000c8818496839240404040", serialize(bean));
	}

	@Test
	public void testRdef03Alt2() {
		Rdef03Record bean = new Rdef03Record();
		bean.setComSelect((short) 1);
		Rdef03Record.ComDetail1Choice choice = new Rdef03Record.ComDetail1Choice();
		Rdef03Record.ComDetail1Choice.ComDetail2 alt2 = new Rdef03Record.ComDetail1Choice.ComDetail2();
		alt2.setComAmount(new BigDecimal("235.38"));
		choice.setComDetail2(alt2);
		bean.setComDetail1Choice(choice);
		assertEquals("00010023538f", serialize(bean));
	}

	@Test
	public void testRdef03Alt3() {
		Rdef03Record bean = new Rdef03Record();
		bean.setComSelect((short) 2);
		Rdef03Record.ComDetail1Choice choice = new Rdef03Record.ComDetail1Choice();
		Rdef03Record.ComDetail1Choice.ComDetail3 alt3 = new Rdef03Record.ComDetail1Choice.ComDetail3();
		alt3.setComNumber(new BigDecimal("12345"));
		choice.setComDetail3(alt3);
		bean.setComDetail1Choice(choice);
		assertEquals("0002f1f2f3f4f5", serialize(bean));
	}

	@Test
	public void testRdef04Alt1() {
		Rdef04Record bean = new Rdef04Record();
		Rdef04Record.OuterRedefinesLongChoice choice = new Rdef04Record.OuterRedefinesLongChoice();
		choice.setOuterRedefinesLong("Long");
		bean.setOuterRedefinesLongChoice(choice);
		bean.setFooter("L");
		assertEquals("d3969587404040404040d3", serialize(bean));
	}

	@Test
	public void testRdef04Alt2() {
		Rdef04Record bean = new Rdef04Record();
		Rdef04Record.OuterRedefinesLongChoice choice = new Rdef04Record.OuterRedefinesLongChoice();
		Rdef04Record.OuterRedefinesLongChoice.OuterRedefinesShort alt2 = new Rdef04Record.OuterRedefinesLongChoice.OuterRedefinesShort();
		Rdef04Record.OuterRedefinesLongChoice.OuterRedefinesShort.InnerRedefinesLongChoice innerChoice = new Rdef04Record.OuterRedefinesLongChoice.OuterRedefinesShort.InnerRedefinesLongChoice();
		innerChoice.setInnerRedefinesLong("ABCDE");
		alt2.setInnerRedefinesLongChoice(innerChoice);
		choice.setOuterRedefinesShort(alt2);
		bean.setOuterRedefinesLongChoice(choice);
		bean.setFooter("S");
		assertEquals("c1c2c3c4c50000000000e2", serialize(bean));
	}

	@Test
	public void testRdef06None() {
		Rdef06Record bean = new Rdef06Record();
		bean.setOptlStructInd(BigDecimal.ZERO);
		assertEquals("f0f0f0", serialize(bean));
	}

	@Test
	public void testRdef06Alt1() {
		Rdef06Record bean = new Rdef06Record();
		bean.setOptlStructInd(BigDecimal.ZERO);
		Rdef06Record.OptlItemChoice choice = new Rdef06Record.OptlItemChoice();
		choice.setOptlItem("Fluctuat nec mergitur");
		bean.setOptlItemChoice(choice);
		assertEquals("f0f0f0c693a483a3a481a340958583409485998789a3a4994040", serialize(bean));
	}

	@Test
	public void testRdef06Alt2Empty() {
		Rdef06Record bean = new Rdef06Record();
		bean.setOptlStructInd(BigDecimal.ZERO);
		Rdef06Record.OptlItemChoice choice = new Rdef06Record.OptlItemChoice();
		choice.setOptlStruct(new Rdef06Record.OptlItemChoice.OptlStruct[] { } );
		bean.setOptlItemChoice(choice);
		assertEquals("f0f0f0", serialize(bean));
	}

	@Test
	public void testRdef06Alt2NonEmpty() {
		Rdef06Record bean = new Rdef06Record();
		bean.setOptlStructInd(BigDecimal.ONE);
		Rdef06Record.OptlItemChoice choice = new Rdef06Record.OptlItemChoice();
		Rdef06Record.OptlItemChoice.OptlStruct struct = new Rdef06Record.OptlItemChoice.OptlStruct();
		struct.setOptlStructField1("Delenda es");
		struct.setOptlStructField2("Cartago");
		choice.setOptlStruct(new Rdef06Record.OptlItemChoice.OptlStruct[] { struct } );
		bean.setOptlItemChoice(choice);
		assertEquals("f0f0f1c48593859584814085a24040404040404040c38199a381", serialize(bean));
	}

	@Test
	public void testRdef07Empty() {
		Rdef07Record bean = new Rdef07Record();
		bean.setComCounter(BigDecimal.ZERO);
		assertEquals("f0f0f0", serialize(bean));
	}

	@Test
	public void testRdef07OneAlt1() {
		Rdef07Record bean = new Rdef07Record();
		bean.setComCounter(BigDecimal.ONE);
		Rdef07Record.ComStruct struct1 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice1 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice1.setComAlt1(new BigDecimal("111"));
		struct1.setComAlt1Choice(choice1);
		bean.setComStruct(new Rdef07Record.ComStruct[] { struct1 });
		assertEquals("f0f0f1111f", serialize(bean));
	}

	@Test
	public void testRdef07OneAlt1OneAlt2() {
		Rdef07Record bean = new Rdef07Record();
		bean.setComCounter(new BigDecimal(2));
		Rdef07Record.ComStruct struct1 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice1 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice1.setComAlt1(new BigDecimal("111"));
		struct1.setComAlt1Choice(choice1);
		Rdef07Record.ComStruct struct2 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice2 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice2.setComAlt2("AA");
		struct2.setComAlt1Choice(choice2);
		bean.setComStruct(new Rdef07Record.ComStruct[] { struct1, struct2 });
		assertEquals("f0f0f2111fc1c1", serialize(bean));
	}

	@Test
	public void testRdef07Full() {
		Rdef07Record bean = new Rdef07Record();
		bean.setComCounter(new BigDecimal(5));
		Rdef07Record.ComStruct struct1 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice1 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice1.setComAlt1(new BigDecimal("111"));
		struct1.setComAlt1Choice(choice1);
		Rdef07Record.ComStruct struct2 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice2 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice2.setComAlt2("AA");
		struct2.setComAlt1Choice(choice2);
		Rdef07Record.ComStruct struct3 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice3 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice3.setComAlt1(new BigDecimal("333"));
		struct3.setComAlt1Choice(choice3);
		Rdef07Record.ComStruct struct4 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice4 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice4.setComAlt2("BB");
		struct4.setComAlt1Choice(choice4);
		Rdef07Record.ComStruct struct5 = new Rdef07Record.ComStruct();
		Rdef07Record.ComStruct.ComAlt1Choice choice5 = new Rdef07Record.ComStruct.ComAlt1Choice();
		choice5.setComAlt1(new BigDecimal("555"));
		struct5.setComAlt1Choice(choice5);
		bean.setComStruct(new Rdef07Record.ComStruct[] { struct1, struct2, struct3, struct4, struct5 });
		assertEquals("f0f0f5111fc1c1333fc2c2555f", serialize(bean));
	}

	@Test
	public void testInvalidField() {
		try {
			InvalidFieldClass bean = new InvalidFieldClass();
			bean.setProlog("123");
			bean.setCustomerId("AB");
			serialize(bean);
			fail();
		} catch (Exception e) {
			String message = e.getMessage();
			assertTrue(message.contains("java.lang.NumberFormatException"));
			assertTrue(message.contains("{Cobol item: 'INVALID-FIELD.CUSTOMER-ID', @offset: 3}"));
		}
	}

	private String serialize(Object bean) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos)) {
			CobolBeanSerializer serializer = new CobolBeanSerializer(new CobolPrimitiveConverter(),
					new CobolClassInfoReflect());
			serializer.serialize(cos, bean);
			return HexFormat.of().formatHex(baos.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@CobolGroup(cobolName = "INVALID-FIELD")
	class InvalidFieldClass {
		@CobolString(cobolName = "PROLOG", charNum = 3)
		private String prolog;

		@CobolZonedDecimal(cobolName = "CUSTOMER-ID", totalDigits = 6)
		private String customerId;

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getProlog() {
			return prolog;
		}

		public void setProlog(String prolog) {
			this.prolog = prolog;
		}
	}

}
