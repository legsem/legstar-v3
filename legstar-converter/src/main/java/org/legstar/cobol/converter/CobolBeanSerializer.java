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
 * Lightweight but not thread safe.
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
	private final CobolBeanSerializerContext context;

	public CobolBeanSerializer(CobolOutputStream cobolOutputStream, CobolPrimitiveConverter primitiveConverter,
			CobolClassInfo classInfo) {
		this.primitiveConverter = primitiveConverter;
		this.classInfo = classInfo;
		this.context = new CobolBeanSerializerContext(cobolOutputStream);
	}

	public void serialize(Object bean) {
		Annotation annotation = classInfo.getCobolItemType(bean.getClass());
		serialize(annotation, bean);
	}

	private void serialize(Annotation annotation, Object bean) {
		context.pushCobolItemType(annotation);
		if (annotation instanceof CobolGroup) {
			serializeGroup(annotation, bean);
		} else if (annotation instanceof CobolChoice) {
			serializeGroup(annotation, bean);
		} else {
			serializePrimitive(annotation, bean);
		}
		context.popCobolItemType();
	}

	private void serializeGroup(Annotation annotation, Object group) {
		CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(group.getClass());
		for (CobolFieldInfo fieldInfo : fieldInfos) {
			Object value = getValue(group, fieldInfo);
			if (value == null) {
				continue;
			}
			Class<?> fieldType = fieldInfo.javaType();
			if (fieldType.isMemberClass()) {
				serialize(fieldInfo.cobolItemType(), value);
			} else if (fieldType.isArray()) {
				int len = Array.getLength(value);
				Class<?> itemType = fieldType.getComponentType();
				for (int i = 0; i < len; i++) {
					Object itemValue = Array.get(value, i);
					if (itemType.isMemberClass()) {
						serialize(fieldInfo.cobolItemType(), itemValue);
					} else if (itemType.isArray()) {
						throw new CobolBeanSerializerException(context, "Multidimensial java arrays are not supported");
					} else {
						serialize(fieldInfo.cobolItemType(), itemValue);
					}
				}
			} else {
				serialize(fieldInfo.cobolItemType(), value);
			}
		}
	}

	private void serializePrimitive(Annotation annotation, Object value) {
		try {
			byte[] buffer = null;
			if (annotation instanceof CobolString) {
				buffer = primitiveConverter.toAlphanum((CobolString) annotation, value);
			} else if (annotation instanceof CobolBinaryNumber) {
				buffer = primitiveConverter.toBinaryNumber((CobolBinaryNumber) annotation, value);
			} else if (annotation instanceof CobolZonedDecimal) {
				buffer = primitiveConverter.toZonedDecimal((CobolZonedDecimal) annotation, value);
			} else if (annotation instanceof CobolPackedDecimal) {
				buffer = primitiveConverter.toPackedDecimal((CobolPackedDecimal) annotation, value);
			} else if (annotation instanceof CobolFloat) {
				buffer = primitiveConverter.toComp_1((CobolFloat) annotation, value);
			} else if (annotation instanceof CobolDouble) {
				buffer = primitiveConverter.toComp_2((CobolDouble) annotation, value);
			} else {
				throw new CobolBeanSerializerException(context, "Unsupported Cobol annotation " + annotation);
			}
			context.cobolOutputStream().write(buffer);
		} catch (IOException | NumberFormatException | CobolPrimitiveConverterException e) {
			throw new CobolBeanSerializerException(context, e);
		}
	}

	/**
	 * Retrieve the value of a field on a parent object.
	 * <p>
	 * Cobol fields are usually mandatory so we provide default values instead of
	 * null.
	 * <p>
	 * If a field is an alternative in a choice, null is interpreted as some other
	 * alternative has been selected so we can ignore this one.
	 * <p>
	 * If field is an array and the value is null, we create an array of minimum
	 * occurrences with default values.
	 * 
	 * @param parent    the parent object
	 * @param fieldInfo the field information
	 * @return the value that should be serialized or null if nothing should be
	 *         serialized
	 */
	private Object getValue(Object parent, CobolFieldInfo fieldInfo) {
		try {
			Method getter = fieldInfo.getter();
			Object value = getter.invoke(parent);
			if (value == null) {
				if (fieldInfo.isAlternative()) {
					// Ignore null alternative
				} else if (fieldInfo.javaType().isArray()) {
					// fill array up to minOccurs
					int len = fieldInfo.cobolArray().minOccurs();
					Class<?> itemType = fieldInfo.javaType().componentType();
					value = Array.newInstance(itemType, len);
					Object defaultValue = defaultValue(itemType);
					for (int i = 0; i < len; i++) {
						Array.set(value, i, defaultValue);
					}
				} else if (fieldInfo.javaType().isMemberClass()) {
					value = classInfo.newInstance(fieldInfo.javaType());
				} else {
					value = defaultPrimitiveValue(fieldInfo.javaType());
				}
			}
			return value;
		} catch (ReflectiveOperationException e) {
			throw new CobolBeanSerializerException(context, e);
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
