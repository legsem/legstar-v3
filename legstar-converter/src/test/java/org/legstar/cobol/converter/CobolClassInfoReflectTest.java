package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.annotation.CobolArray;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;
import org.legstar.cobol.base.test.CobolTestBase;

public class CobolClassInfoReflectTest extends CobolTestBase {

	@Test
	public void notAnAnnotatedClass() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		assertEquals(0, classInfo.fieldInfos(NotAnAnnotatedClass.class).length);
	}

	@Test
	public void noFields() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		assertEquals(0, classInfo.fieldInfos(NoFieldsClass.class).length);
	}

	@Test
	public void onePrimitiveField() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		CobolFieldInfo[] cfi = classInfo.fieldInfos(OnePrimitiveFieldClass.class);
		assertEquals(1, cfi.length);
		assertEquals("customerId", cfi[0].name());
		assertEquals(
				"@org.legstar.cobol.annotation.CobolZonedDecimal(signSeparate=false, signed=false, fractionDigits=0, odoObject=false, blankWhenZero=false, signLeading=false, cobolName=\"CUSTOMER-ID\", totalDigits=6)",
				cfi[0].cobolItemType().toString());
		assertEquals("java.math.BigDecimal", cfi[0].javaType().getName());
		assertEquals("getCustomerId", cfi[0].getter().getName());
	}

	@Test
	public void oneComplexField() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		CobolFieldInfo[] cfi = classInfo.fieldInfos(OneComplexFieldClass.class);
		assertEquals(1, cfi.length);
		assertEquals("personalData", cfi[0].name());
		assertEquals("@org.legstar.cobol.annotation.CobolGroup(cobolName=\"PERSONAL-DATA\")", cfi[0].cobolItemType().toString());
		assertEquals("org.legstar.cobol.converter.CobolClassInfoReflectTest$OneComplexFieldClass$PersonalData",
				cfi[0].javaType().getName());
		assertEquals("getPersonalData", cfi[0].getter().getName());
	}

	@Test
	public void arrayFields() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		CobolFieldInfo[] cfi = classInfo.fieldInfos(ArrayFieldsClass.class);
		assertEquals(2, cfi.length);
		assertEquals("optlStruct", cfi[0].name());
		assertEquals("@org.legstar.cobol.annotation.CobolGroup(cobolName=\"OPTL-STRUCT\")", cfi[0].cobolItemType().toString());
		assertEquals("[Lorg.legstar.cobol.converter.CobolClassInfoReflectTest$ArrayFieldsClass$OptlStruct;",
				cfi[0].javaType().getName());
		assertEquals("getOptlStruct", cfi[0].getter().getName());
		assertEquals(
				"@org.legstar.cobol.annotation.CobolArray(dependingOn=\"OPTL-STRUCT-IND\", minOccurs=0, maxOccurs=1)",
				cfi[0].cobolArray().toString());
		assertEquals("optlItem", cfi[1].name());
		assertEquals("@org.legstar.cobol.annotation.CobolString(cobolName=\"OPTL-ITEM\", charNum=32)",
				cfi[1].cobolItemType().toString());
		assertEquals("[Ljava.lang.String;", cfi[1].javaType().getName());
		assertEquals("getOptlItem", cfi[1].getter().getName());
		assertEquals(
				"@org.legstar.cobol.annotation.CobolArray(dependingOn=\"OPTL-ITEM-IND\", minOccurs=0, maxOccurs=1)",
				cfi[1].cobolArray().toString());
	}

	@Test
	public void oneChoice() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		CobolFieldInfo[] cfi = classInfo.fieldInfos(ChoiceClass.class);
		assertEquals(2, cfi.length);
		assertEquals("choice1Alt1", cfi[0].name());
		assertTrue(cfi[0].isAlternative());
		assertEquals("choice1Alt2", cfi[1].name());
		assertTrue(cfi[1].isAlternative());
	}
	
	@Test
	public void testCaching() {
		CobolClassInfoReflect classInfo = new CobolClassInfoReflect();
		CobolFieldInfo[] cfi = classInfo.fieldInfos(OnePrimitiveFieldClass.class);
		CobolFieldInfo[] cfi2 = classInfo.fieldInfos(OnePrimitiveFieldClass.class);
		assertEquals(cfi, cfi2);
	}

	class NotAnAnnotatedClass {

	}

	@CobolGroup(cobolName = "NO-FIELDS")
	class NoFieldsClass {

	}

	@CobolGroup(cobolName = "ONE-PRIMTIVE-FIELD")
	class OnePrimitiveFieldClass {
		@CobolZonedDecimal(cobolName = "CUSTOMER-ID", totalDigits = 6)
		private BigDecimal customerId;

		public BigDecimal getCustomerId() {
			return customerId;
		}

		public void setCustomerId(BigDecimal customerId) {
			this.customerId = customerId;
		}
	}

	@CobolGroup(cobolName = "ONE-COMPLEX-FIELD")
	class OneComplexFieldClass {
		private PersonalData personalData;

		public PersonalData getPersonalData() {
			return personalData;
		}

		public void setPersonalData(PersonalData personalData) {
			this.personalData = personalData;
		}

		@CobolGroup(cobolName = "PERSONAL-DATA")
		public static class PersonalData {
			@CobolString(cobolName = "CUSTOMER-NAME", charNum = 20)
			private String customerName;

			@CobolString(cobolName = "CUSTOMER-ADDRESS", charNum = 20)
			private String customerAddress;

			@CobolString(cobolName = "CUSTOMER-PHONE", charNum = 8)
			private String customerPhone;

			public String getCustomerName() {
				return customerName;
			}

			public void setCustomerName(String customerName) {
				this.customerName = customerName;
			}

		}
	}

	@CobolGroup(cobolName = "ARRAY-FIELDS")
	class ArrayFieldsClass {
		@CobolArray(minOccurs = 0, maxOccurs = 1, dependingOn = "OPTL-STRUCT-IND")
		private OptlStruct[] optlStruct;

		@CobolArray(minOccurs = 0, maxOccurs = 1, dependingOn = "OPTL-ITEM-IND")
		@CobolString(cobolName = "OPTL-ITEM", charNum = 32)
		private String[] optlItem;

		public OptlStruct[] getOptlStruct() {
			return optlStruct;
		}

		public void setOptlStruct(OptlStruct[] optlStruct) {
			this.optlStruct = optlStruct;
		}

		public String[] getOptlItem() {
			return optlItem;
		}

		public void setOptlItem(String[] optlItem) {
			this.optlItem = optlItem;
		}

		@CobolGroup(cobolName = "OPTL-STRUCT")
		public static class OptlStruct {

			@CobolString(cobolName = "OPTL-STRUCT-FIELD1", charNum = 18)
			private String optlStructField1;

			@CobolString(cobolName = "OPTL-STRUCT-FIELD2", charNum = 5)
			private String optlStructField2;

			public String getOptlStructField1() {
				return optlStructField1;
			}

			public void setOptlStructField1(String optlStructField1) {
				this.optlStructField1 = optlStructField1;
			}

			public String getOptlStructField2() {
				return optlStructField2;
			}

			public void setOptlStructField2(String optlStructField2) {
				this.optlStructField2 = optlStructField2;
			}

		}

	}

	@CobolChoice(cobolName = "CHOICE1-ALT1", maxBytesLen = 4)
	static class ChoiceClass {

		@CobolString(cobolName = "CHOICE1-ALT1", charNum = 4)
		private String choice1Alt1;

		@CobolZonedDecimal(cobolName = "CHOICE1-ALT2", totalDigits = 4, fractionDigits = 3)
		private BigDecimal choice1Alt2;

		public String getChoice1Alt1() {
			return choice1Alt1;
		}

		public void setChoice1Alt1(String choice1Alt1) {
			this.choice1Alt1 = choice1Alt1;
		}

		public BigDecimal getChoice1Alt2() {
			return choice1Alt2;
		}

		public void setChoice1Alt2(BigDecimal choice1Alt2) {
			this.choice1Alt2 = choice1Alt2;
		}

	}
}
