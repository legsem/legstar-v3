package org.legstar.cobol.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;
import org.legstar.cobol.io.CobolOutputStream;

/**
 * Serializes a Cobol-annotated java bean into a cobol output stream.
 * <p>
 * Lightweight but not thread safe. This class is mutable.
 * <p>
 * For arrays depending on (ODO) there are no controls in here. It is up to the
 * caller to pass a bean where the dimension of an ODO is consistent with the
 * actual array size.
 * <p>
 * For choices, it is up to the caller to pass a bean where one and only one of
 * the alternatives is not null.
 */
public class CobolBeanSerializer {

	/**
	 * Converter for primitive types.
	 */
	private final CobolPrimitiveConverter primitiveConverter;

	/**
	 * Class/Field cache
	 */
	private final CobolClassInfo classInfo;

	/**
	 * Mutable serialization context.
	 */
	private CobolBeanSerializerContext context;

	/**
	 * Create a Cobol bean serializer.
	 * 
	 * @param primitiveConverter the Cobol primitive types converters
	 * @param classInfo          the source bean description
	 */
	public CobolBeanSerializer(CobolPrimitiveConverter primitiveConverter, CobolClassInfo classInfo) {
		this.primitiveConverter = primitiveConverter;
		this.classInfo = classInfo;
	}

	/**
	 * Serialize a bean as Cobol data.
	 * <p>
	 * Bean must not be an array. Arrays are only treated as fields later in this
	 * code.
	 * <p>
	 * Collections of beans must be dealt with upstream by calling this method
	 * iteratively passing the same CobolOutputStream each time.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param bean              the bean to be serialized as Cobol data
	 */
	public void serialize(CobolOutputStream cobolOutputStream, Object bean) {
		context = new CobolBeanSerializerContext(cobolOutputStream);
		Annotation cobolItemType = classInfo.getCobolItemType(bean.getClass());
		serialize(cobolItemType, bean);
	}

	/**
	 * Serialize a java value to a specific Cobol item type data.
	 * 
	 * @param cobolItemType the kind of Cobol item to serialize to
	 * @param value         the java value
	 */
	private void serialize(Annotation cobolItemType, Object value) {
		context.pushCobolItemType(cobolItemType);
		if (cobolItemType instanceof CobolGroup) {
			serializeGroup((CobolGroup) cobolItemType, value);
		} else if (cobolItemType instanceof CobolChoice) {
			serializeChoice((CobolChoice) cobolItemType, value);
		} else {
			serializePrimitive(cobolItemType, value);
		}
		context.popCobolItemType();
	}

	/**
	 * Serialize a group.
	 * <p>
	 * Cobol fields in a group item are mandatory so we provide default values
	 * instead of null.
	 * <p>
	 * If field is an array and the value is null, we create an array of minimum
	 * occurrences with default values.
	 * 
	 * @param cobolItemType the Cobol item type
	 * @param group         the group object
	 */
	private void serializeGroup(CobolGroup cobolItemType, Object group) {
		CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(group.getClass());
		for (CobolFieldInfo fieldInfo : fieldInfos) {
			Object value = getValue(group, fieldInfo);
			if (value == null) {
				value = defaultValue(fieldInfo);
			}
			serializeField(fieldInfo, value);
		}
	}

	/**
	 * Serialize a choice.
	 * <p>
	 * If a field is an alternative in a choice, null is interpreted as some other
	 * alternative has been selected so we can ignore this one.
	 * <p>
	 * First non-null choice is the chosen alternative.
	 * <p>
	 * If the chosen alternative does not fill the choice length and choice is not
	 * the last field, then we need to fill the choice length so that whatever
	 * follows the choice is not variably located. So here we let the context know
	 * that there are leftover bytes to be written (unless this is the last field).
	 * 
	 * @param cobolItemType the Cobol item type
	 * @param choice        the choice object
	 */
	private void serializeChoice(CobolChoice cobolItemType, Object choice) {
		int choiceLen = cobolItemType.maxBytesLen();
		long start = context.bytesCounter();
		CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(choice.getClass());
		for (CobolFieldInfo fieldInfo : fieldInfos) {
			Object value = getValue(choice, fieldInfo);
			if (value == null) {
				continue;
			}
			serializeField(fieldInfo, value);
			break;
		}
		long end = context.bytesCounter();
		context.setLeftover(choiceLen - (end - start));
	}

	/**
	 * Serialize a field.
	 * <p>
	 * A field is the child of either a group or a choice.
	 * 
	 * @param fieldInfo the field information
	 * @param value     the field's value
	 */
	private void serializeField(CobolFieldInfo fieldInfo, Object value) {
		Class<?> fieldType = fieldInfo.javaType();
		if (fieldType.isMemberClass()) {
			serialize(fieldInfo.cobolItemType(), value);
		} else if (fieldType.isArray()) {
			serializeArray(fieldInfo.cobolItemType(), value);
		} else {
			serialize(fieldInfo.cobolItemType(), value);
		}
	}

	/**
	 * Serialize an array.
	 * <p>
	 * Amounts to serializing each of the items in turn.
	 * 
	 * @param cobolItemType the Cobol item type
	 * @param array         the array
	 */
	private void serializeArray(Annotation cobolItemType, Object array) {
		int len = Array.getLength(array);
		Class<?> itemType = array.getClass().getComponentType();
		for (int i = 0; i < len; i++) {
			Object itemValue = Array.get(array, i);
			if (itemType.isMemberClass()) {
				serialize(cobolItemType, itemValue);
			} else if (itemType.isArray()) {
				throw new CobolBeanSerializerException(context, "Multidimensial java arrays are not supported");
			} else {
				serialize(cobolItemType, itemValue);
			}
		}
	}

	/**
	 * Serialize a primitive type.
	 * <p>
	 * Actually writing cobol bytes only happens for primitive types.
	 * 
	 * @param cobolItemType the Cobol item type
	 * @param value         the primitive value
	 */
	private void serializePrimitive(Annotation cobolItemType, Object value) {
		try {
			byte[] buffer = null;
			if (cobolItemType instanceof CobolString) {
				buffer = primitiveConverter.toAlphanum((CobolString) cobolItemType, value);
			} else if (cobolItemType instanceof CobolBinaryNumber) {
				buffer = primitiveConverter.toBinaryNumber((CobolBinaryNumber) cobolItemType, value);
			} else if (cobolItemType instanceof CobolZonedDecimal) {
				buffer = primitiveConverter.toZonedDecimal((CobolZonedDecimal) cobolItemType, value);
			} else if (cobolItemType instanceof CobolPackedDecimal) {
				buffer = primitiveConverter.toPackedDecimal((CobolPackedDecimal) cobolItemType, value);
			} else if (cobolItemType instanceof CobolFloat) {
				buffer = primitiveConverter.toComp_1((CobolFloat) cobolItemType, value);
			} else if (cobolItemType instanceof CobolDouble) {
				buffer = primitiveConverter.toComp_2((CobolDouble) cobolItemType, value);
			} else {
				throw new CobolBeanSerializerException(context, "Unsupported Cobol annotation " + cobolItemType);
			}
			write(buffer);
		} catch (NumberFormatException | CobolPrimitiveConverterException e) {
			throw new CobolBeanSerializerException(context, e);
		}
	}

	/**
	 * Output a Cobol byte buffer.
	 * <p>
	 * If there are some left over bytes to be written start by outputting those as
	 * low values.
	 * 
	 * @param buffer the Cobol byte buffer
	 */
	private void write(byte[] buffer) {
		try {
			if (context.getLeftover() > 0) {
				for (long l = 0; l < context.getLeftover(); l++) {
					context.cobolOutputStream().write(0);
				}
				context.setLeftover(0);
			}
			context.cobolOutputStream().write(buffer);
		} catch (IOException e) {
			throw new CobolBeanSerializerException(context, e);
		}
	}

	/**
	 * Retrieve the value of a field on a parent object.
	 * 
	 * @param parent    the parent object
	 * @param fieldInfo the field information
	 * @return the value that should be serialized or null
	 */
	private Object getValue(Object parent, CobolFieldInfo fieldInfo) {
		try {
			Method getter = fieldInfo.getter();
			return getter.invoke(parent);
		} catch (ReflectiveOperationException e) {
			throw new CobolBeanSerializerException(context, e);
		}
	}

	/**
	 * Given a field, get a valid default value.
	 * <p>
	 * If field is an array and the value is null, we create an array of minimum
	 * occurrences with default values.
	 * 
	 * @param fieldInfo the Field description
	 * @return a valid default value
	 */
	private Object defaultValue(CobolFieldInfo fieldInfo) {
		if (fieldInfo.javaType().isArray()) {
			int len = fieldInfo.cobolArray().minOccurs();
			Class<?> itemType = fieldInfo.javaType().componentType();
			Object array = Array.newInstance(itemType, len);
			Object defaultValue = defaultValue(itemType);
			for (int i = 0; i < len; i++) {
				Array.set(array, i, defaultValue);
			}
			return array;
		} else if (fieldInfo.javaType().isMemberClass()) {
			return classInfo.newInstance(fieldInfo.javaType());
		} else {
			return defaultPrimitiveValue(fieldInfo.javaType());
		}
	}

	/**
	 * Given a java type return a default value.
	 * <p>
	 * javaType must not be an array. It must be either an inner class or a
	 * primitive.
	 * 
	 * @param javaType the java type
	 * @return a default value
	 */
	private Object defaultValue(Class<?> javaType) {
		if (javaType.isArray()) {
			throw new CobolBeanSerializerException(context, "Multidimensial java arrays are not supported");
		} else if (javaType.isMemberClass()) {
			return classInfo.newInstance(javaType);
		} else {
			return defaultPrimitiveValue(javaType);
		}
	}

	/**
	 * Assuming a primitive type, return a default value.
	 * 
	 * @param javaType the java type
	 * @return either zero or an empty string
	 */
	private Object defaultPrimitiveValue(Class<?> javaType) {
		if (javaType == BigDecimal.class) {
			return BigDecimal.valueOf(0);
		} else if (javaType == Short.class) {
			return Short.valueOf((short) 0);
		} else if (javaType == Integer.class) {
			return Integer.valueOf(0);
		} else if (javaType == Long.class) {
			return Long.valueOf(0);
		} else if (javaType == Float.class) {
			return Float.valueOf(0);
		} else if (javaType == Double.class) {
			return Double.valueOf(0);
		} else {
			return "";
		}
	}

}
