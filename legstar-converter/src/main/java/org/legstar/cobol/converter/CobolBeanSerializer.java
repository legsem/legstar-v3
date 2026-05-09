package org.legstar.cobol.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.legstar.cobol.converter.CobolConverterConfig;
import org.legstar.cobol.io.CobolOutputStream;

/**
 * Serializes a Cobol-annotated java bean into a cobol output stream.
 */
public class CobolBeanSerializer extends CobolPrimitiveSerializer {

	private final CobolClassInfo classInfo = new CobolClassInfoReflect();

	public CobolBeanSerializer(CobolConverterConfig config) {
		super(config);
	}

	public void serialize(CobolOutputStream cos, Object bean) {
		try {
			CobolClassInfo.FieldInfo[] fieldInfos = classInfo.fieldInfos(bean.getClass());
			for (CobolClassInfo.FieldInfo fieldInfo : fieldInfos) {
				System.out.println("---------------------------------");
				System.out.println(fieldInfo.name());
				System.out.println(fieldInfo.cobolItemType());
				Method getter = fieldInfo.getter();
				Object value = getter.invoke(bean);
				if (value == null) {
					continue;
				}
				Class<?> fieldType = fieldInfo.javaType();
				if (fieldType.isMemberClass()) {
					serialize(cos, value);
				} else if (fieldType.isArray()) {
					int len = Array.getLength(value);
					fieldType.getComponentType();
					for (int i = 0; i < len; i++) {
						Object itemValue = Array.get(value, i);
						serialize(cos, itemValue);
					}
				} else {
					serialize(cos, fieldInfo.cobolItemType(), value);
				}
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

}
