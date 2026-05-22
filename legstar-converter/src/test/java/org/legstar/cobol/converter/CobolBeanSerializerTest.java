package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HexFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.legstar.cobol.io.CobolOutputStream;

import legstar.samples.alltypes.Alltypes;
import legstar.samples.custdat.CustomerData;
import legstar.samples.flat01.Flat01Record;
import legstar.samples.flat02.Flat02Record;
import legstar.samples.stru01.Stru01Record;
import legstar.samples.stru03.Stru03Record;

public class CobolBeanSerializerTest extends CobolConverterTestBase {
	
	CobolOutputStream cos;
	
	ByteArrayOutputStream baos;

	@BeforeEach
	private void setUp() {
		baos = new ByteArrayOutputStream();
		cos = new CobolOutputStream(baos);
	}
	
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
		bean.setComArray(new Short[] {5, 6, 7, 8, 9});
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f00050006000700080009", serialize(bean));
	}

	@Test
	public void testFlat02Empty() {
		Flat02Record bean = new Flat02Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000000f00000000000000000000", serialize(bean));
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
		bean.setComArray(new Stru03Record.ComArray[] {subItem1, subItem2, subItem3, subItem4, subItem5});
		assertEquals("f0f0f8f9f5f6c195a39695899640c2a4a2a385939396404040400023556f0117d1e40118d1e50119d1e6011ad1e7011bd1e8", serialize(bean));
	}

	@Test
	public void testStru03Empty() {
		Stru03Record bean = new Stru03Record();
		assertEquals("f0f0f0f0f0f040404040404040404040404040404040404040400000000f0000404000004040000040400000404000004040", serialize(bean));
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
		assertEquals("c3819993c289954000ff7ffffffea2f37fffffff000033bebfeea6787fffffffffffffff000026521f4d20497d04fe6032808def80f2f5f6f5d6f2f5f6f5f6d2f5f6f5d660f6f5f6604040404040", serialize(bean));
	}

	@Test
	public void testAlltypesEmpty() {
		Alltypes bean = new Alltypes();
		assertEquals("404040404040404000000000000000000000000000000000000000000000000000000000000000000f000000000000000000000000f0f0f0f0c0f0f0f0f0f0c0f0f0f0c04ef0f0f04e4040404040", serialize(bean));
	}
	@Test
	public void testCustomerData() {
		CustomerData bean = new CustomerData();
		bean.setCustomerId(new BigDecimal(72));
		assertEquals("f0f0f0f0f7f240404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404000000000", serialize(bean));
	}
	
	private String serialize(Object bean) {
		CobolBeanSerializer serializer = new CobolBeanSerializer();
		serializer.serialize(cos, bean);
		return HexFormat.of().formatHex(baos.toByteArray());
	}

}
