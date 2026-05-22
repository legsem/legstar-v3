package org.legstar.cobol.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.legstar.cobol.io.CobolOutputStream;

/**
 * Serializes a Cobol-annotated java bean into a cobol output stream.
 */
public class CobolBeanSerializer extends CobolPrimitiveSerializer {

	private final CobolClassInfo classInfo = new CobolClassInfoReflect();

	public CobolBeanSerializer() {
		this(CobolBeanConverterConfig.ebcdic());
	}

	public CobolBeanSerializer(CobolConverterConfig config) {
		super(config);
	}

	public void serialize(CobolOutputStream cos, Object bean) {
		CobolFieldInfo[] fieldInfos = classInfo.fieldInfos(bean.getClass());
		for (CobolFieldInfo fieldInfo : fieldInfos) {
			Object value = getValue(bean, fieldInfo);
			if (value == null) {
				continue;
			}
			Class<?> fieldType = fieldInfo.javaType();
			if (fieldType.isMemberClass()) {
				serialize(cos, value);
			} else if (fieldType.isArray()) {
				int len = Array.getLength(value);
				Class<?> itemType = fieldType.getComponentType();
				for (int i = 0; i < len; i++) {
					Object itemValue = Array.get(value, i);
					if (itemType.isMemberClass()) {
						serialize(cos, itemValue);
					} else if (itemType.isArray()) {
						// TODO process sub items
					} else {
						serialize(cos, fieldInfo.cobolItemType(), itemValue);
					}
					// TODO if len < minOccurs, fill with default values
				}
			} else {
				serialize(cos, fieldInfo.cobolItemType(), value);
			}
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
			throw new RuntimeException(e);
		}

	}

	/**
	 * Given a java type return a default value.
	 * <p>
	 * javaType must not be an array (can't allocate an array without a length)
	 * 
	 * @param javaType the java type
	 * @return a default value
	 */
	private Object defaultValue(Class<?> javaType) {
		if (javaType.isArray()) {
			return null;
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
