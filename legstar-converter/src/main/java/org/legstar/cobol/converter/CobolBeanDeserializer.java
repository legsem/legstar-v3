package org.legstar.cobol.converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.legstar.cobol.annotation.CobolArray;
import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;
import org.legstar.cobol.io.CobolInputStream;

/**
 * De-serializes Cobol data into a Java bean.
 * 
 * @param <T> target java bean type
 */
public class CobolBeanDeserializer<T> {

	/**
	 * Converter for primitive types.
	 */
	private final CobolPrimitiveConverter primitiveConverter;

	/**
	 * Class/Field cache
	 */
	private final CobolClassInfo classInfo;

	/**
	 * A custom choice resolution strategy. Null if default strategy applies
	 */
	private final CobolChoiceStrategy<T> choiceStrategy;

	/**
	 * Mutable deserialization context.
	 */
	private CobolBeanDeserializerContext context;

	/**
	 * Create a Cobol bean de-serializer using the default choice resolution strategy.
	 * 
	 * @param primitiveConverter the Cobol primitive types converters
	 * @param classInfo          the source bean description
	 */
	public CobolBeanDeserializer(CobolPrimitiveConverter primitiveConverter, CobolClassInfo classInfo) {
		this(primitiveConverter, classInfo, new CobolDefaultChoiceStrategy<>());
	}

	/**
	 * Create a Cobol bean de-serializer.
	 * 
	 * @param primitiveConverter the Cobol primitive types converters
	 * @param classInfo          the source bean description
	 * @param choiceStrategy     strategy to select alternatives in choices
	 */
	public CobolBeanDeserializer(CobolPrimitiveConverter primitiveConverter, CobolClassInfo classInfo,
			CobolChoiceStrategy<T> choiceStrategy) {
		this.primitiveConverter = primitiveConverter;
		this.classInfo = classInfo;
		this.choiceStrategy = choiceStrategy;
	}

	/**
	 * De-serialize Cobol data as a Java bean.
	 * <p>
	 * Bean must not be an array. Arrays are only treated as fields later in this
	 * code.
	 * <p>
	 * Collections of beans must be dealt with upstream by calling this method
	 * iteratively passing the same CobolInputStream each time.
	 * 
	 * @param cobolInputStream the Cobol input data stream
	 * @param beanClass        the target java class
	 * @return the converted java value
	 */
	public T deserialize(CobolInputStream cobolInputStream, Class<T> beanClass) {
		context = new CobolBeanDeserializerContext(cobolInputStream);
		Annotation cobolItemType = classInfo.getCobolItemType(beanClass);
		return deserialize(cobolItemType, beanClass);
	}

	/**
	 * De-serialize a specific Cobol item type data as a java value.
	 * 
	 * @param cobolItemType the kind of Cobol item to deserialize from
	 * @param objectClass   the target java class
	 */
	private <Z> Z deserialize(Annotation cobolItemType, Class<Z> objectClass) {
		context.pushCobolItemType(cobolItemType);
		Z value = null;
		if (cobolItemType instanceof CobolGroup) {
			value = deserializeGroup((CobolGroup) cobolItemType, objectClass);
		} else if (cobolItemType instanceof CobolChoice) {
			value = deserializeChoice((CobolChoice) cobolItemType, objectClass);
		} else {
			value = deserializePrimitive(cobolItemType, objectClass);
		}
		context.popCobolItemType();
		return value;
	}

	/**
	 * De-serialize a Cobol group.
	 * 
	 * @param <Z>        the target java type
	 * @param cobolGroup the Cobol group annotation
	 * @param groupClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z deserializeGroup(CobolGroup cobolItemType, Class<Z> groupClass) {
		Z group = classInfo.newInstance(groupClass);
		context.pushGroup(group);
		CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(groupClass);
		for (CobolFieldInfo fieldInfo : fieldInfos) {
			Object value = deserializeField(fieldInfo);
			setFieldValue(group, fieldInfo, value);
		}
		context.popGroup();
		return group;
	}

	/**
	 * De-serialize a Cobol choice.
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolChoice the Cobol choice annotation
	 * @param choiceClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z deserializeChoice(CobolChoice cobolChoice, Class<Z> choiceClass) {
		try {
			Z choice = classInfo.newInstance(choiceClass);
			CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(choiceClass);
			long start = context.getBytesRead();
			for (CobolFieldInfo fieldInfo : fieldInfos) {
				if (choiceStrategy.choose(context.getRoot(), choice, fieldInfo)) {
					// Strategy says this alternative is eligible so we try it out
					context.mark(cobolChoice.maxBytesLen());
					try {
						Object value = deserializeField(fieldInfo);
						// Alternative matches the data (no exception caught while converting)
						setFieldValue(choice, fieldInfo, value);
						long leftover = cobolChoice.maxBytesLen() - (context.getBytesRead() - start);
						if (leftover > 0) {
							// Chosen alternative is shorter than the larger one
							context.skip(leftover);
						}
						break;
					} catch (Exception e) {
						// Alternative does not match the data, try another one
						context.reset();
					}
				}
			}
			if (start == context.getBytesRead()) {
				// No data was consumed means no alternative matched the data
				throw new CobolBeanDeserializerException(context,
						"None of the " + fieldInfos.length + " alternatives matched the data");
			}
			return choice;
		} catch (IOException e) {
			throw new CobolBeanDeserializerException(context, e);
		}
	}

	/**
	 * De-serialize a group child item.
	 * 
	 * @param fieldInfo the field information
	 * @return the converted java value
	 */
	private Object deserializeField(CobolFieldInfo fieldInfo) {
		Annotation cobolItemType = fieldInfo.cobolItemType();
		if (cobolItemType == null) {
			throw new CobolBeanDeserializerException(context,
					"Field " + fieldInfo.name() + " does not have Cobol annotations");
		}
		CobolArray cobolArray = fieldInfo.cobolArray();
		return cobolArray == null //
				? deserialize(cobolItemType, fieldInfo.javaType()) //
				: serializeArray(cobolArray, cobolItemType, fieldInfo.javaType().getComponentType());
	}

	/**
	 * De-serialize an array of items.
	 * 
	 * @param <Z>           the target java item type
	 * @param cobolArray    the Cobol array annotation
	 * @param cobolItemType the Cobol item type annotation
	 * @param itemClass     the target java item class
	 * @return the converted java array value
	 */
	private <Z> Z[] serializeArray(CobolArray cobolArray, Annotation cobolItemType, Class<Z> itemClass) {
		int maxOccurs = cobolArray.dependingOn() == null || cobolArray.dependingOn().isBlank() //
				? cobolArray.maxOccurs() //
				: getOdoObjectValue(cobolArray.dependingOn(), cobolArray.maxOccurs());
		@SuppressWarnings("unchecked")
		Z[] array = (Z[]) Array.newInstance(itemClass, maxOccurs);
		for (int i = 0; i < maxOccurs; i++) {
			array[i] = deserialize(cobolItemType, itemClass);
		}
		return array;
	}

	/**
	 * Set a field value using a setter method on the parent object.
	 * 
	 * @param fieldInfo the field information
	 * @param parent    the parent object
	 * @param value     the value to set
	 */
	private <Z> void setFieldValue(Z parent, CobolFieldInfo fieldInfo, Object value) {
		try {
			Method setter = fieldInfo.setter();
			setter.invoke(parent, value);
		} catch (ReflectiveOperationException e) {
			throw new CobolBeanDeserializerException(context, e);
		}
	}

	/**
	 * Deserialize a primitive type.
	 * <p>
	 * Numeric values may give the actual size of a variable size array. Here we
	 * store such values in the context for later referral.
	 * 
	 * @param <Z>           the primitive java type
	 * @param cobolItemType the Cobol item type
	 * @param objectClass   the primitive java class
	 * @return the primitive java value
	 */
	private <Z> Z deserializePrimitive(Annotation cobolItemType, Class<Z> objectClass) {
		try {
			InputStream is = context.cobolInputStream();
			Z value = null;
			boolean odoObject = false;
			String cobolName = null;
			if (cobolItemType instanceof CobolString) {
				value = primitiveConverter.toJava(is, (CobolString) cobolItemType, objectClass);
			} else if (cobolItemType instanceof CobolBinaryNumber) {
				CobolBinaryNumber cobolBinaryNumber = (CobolBinaryNumber) cobolItemType;
				odoObject = cobolBinaryNumber.odoObject();
				cobolName = cobolBinaryNumber.cobolName();
				value = primitiveConverter.toJava(is, cobolBinaryNumber, objectClass);
			} else if (cobolItemType instanceof CobolZonedDecimal) {
				CobolZonedDecimal cobolZonedDecimal = (CobolZonedDecimal) cobolItemType;
				odoObject = cobolZonedDecimal.odoObject();
				cobolName = cobolZonedDecimal.cobolName();
				value = primitiveConverter.toJava(is, cobolZonedDecimal, objectClass);
			} else if (cobolItemType instanceof CobolPackedDecimal) {
				CobolPackedDecimal cobolPackedDecimal = (CobolPackedDecimal) cobolItemType;
				odoObject = cobolPackedDecimal.odoObject();
				cobolName = cobolPackedDecimal.cobolName();
				value = primitiveConverter.toJava(is, cobolPackedDecimal, objectClass);
			} else if (cobolItemType instanceof CobolFloat) {
				value = primitiveConverter.toJava(is, (CobolFloat) cobolItemType, objectClass);
			} else if (cobolItemType instanceof CobolDouble) {
				value = primitiveConverter.toJava(is, (CobolDouble) cobolItemType, objectClass);
			} else {
				throw new CobolBeanDeserializerException(context, "Unsupported Cobol annotation " + cobolItemType);
			}
			if (odoObject) {
				setOdoObjectValue(cobolName, value);
			}
			return value;
		} catch (NumberFormatException | CobolPrimitiveConverterException e) {
			throw new CobolBeanDeserializerException(context, e);
		}
	}

	// -----------------------------------------------------------------------------
	// Occurs depending On
	// -----------------------------------------------------------------------------
	/**
	 * Given a numeric value set the corresponding variable size Cobol array size.
	 * 
	 * @param <Z>       the value type
	 * @param cobolName Cobol name of the variable giving the array size (Occurs
	 *                  depending on object).
	 * @param value     the value
	 */
	private <Z> void setOdoObjectValue(String cobolName, Z value) {
		if (value instanceof Number) {
			context.putOdoObjectValue(cobolName, ((Number) value).intValue());
		} else {
			throw new CobolBeanDeserializerException(context,
					"Value " + value + " is not a number and cannot be used as an array size");
		}
	}

	/**
	 * Retrieve the actual size of a variable size Cobol array.
	 * 
	 * @param dependingOn Cobol name of the variable giving the array size (Occurs
	 *                    depending on object).
	 * @param maxOccurs   the maximum size of the array
	 * @return the current size of the array
	 */
	private int getOdoObjectValue(String dependingOn, int maxOccurs) {
		Integer value = context.getOdoObjectValue(dependingOn);
		if (value == null) {
			throw new CobolBeanDeserializerException(context,
					"Array size depends on " + dependingOn + " but no value was set");
		} else if (value < 0 || value > maxOccurs) {
			throw new CobolBeanDeserializerException(context,
					"Array size depending on " + dependingOn + " is '" + value + "' which is invalid");
		} else {
			return value;
		}
	}

}
