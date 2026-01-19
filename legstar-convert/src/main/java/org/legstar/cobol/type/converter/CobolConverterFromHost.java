package org.legstar.cobol.type.converter;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.legstar.cobol.type.annotations.CobolArray;
import org.legstar.cobol.type.annotations.CobolBinary;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolItemType;
import org.legstar.cobol.type.annotations.CobolPackedDecimal;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

public class CobolConverterFromHost<T> {

	private final CobolConverterString stringConverter;

	private final CobolConverterBinary binaryConverter;

	private final CobolConverterZonedDecimal zonedDecimalConverter;
	
	private final CobolConverterPackedDecimal packedDecimalConverter;
	
	private final Map<String, Integer> odoObjectValues = new HashMap<>();

	public CobolConverterFromHost(CobolConverterConfig config) {
		stringConverter = new CobolConverterString(config);
		binaryConverter = new CobolConverterBinary(config);
		zonedDecimalConverter = new CobolConverterZonedDecimal(config);
		packedDecimalConverter = new CobolConverterPackedDecimal(config);
	}

	public T convert(InputStream is, Class<T> outputClass) {
		try {
			Annotation annotation = getCobolItemType(outputClass);
			return convert(annotation, is, outputClass);
		} catch (Exception e) {
			throw new CobolConverterException(e);
		}
	}

	private <Z> Z convert(Annotation annotation, InputStream is, Class<Z> objectClass) {
		if (annotation instanceof CobolGroup) {
			return convertGroup((CobolGroup) annotation, is, objectClass);
		} else if (annotation instanceof CobolString) {
			return convertString((CobolString) annotation, is, objectClass);
		} else if (annotation instanceof CobolBinary) {
			return convertBinary((CobolBinary) annotation, is, objectClass);
		} else if (annotation instanceof CobolZonedDecimal) {
			return convertZonedDecimal((CobolZonedDecimal) annotation, is, objectClass);
		} else if (annotation instanceof CobolPackedDecimal) {
			return convertPackedDecimal((CobolPackedDecimal) annotation, is, objectClass);
		} else {
			throw new CobolConverterException("Unsupported Cobol annotation " + annotation);
		}
	}

	private <Z> Z convertGroup(CobolGroup cobolGroup, InputStream is, Class<Z> groupClass) {
		Z group = newInstance(groupClass);
		Field[] fields = groupClass.getDeclaredFields();
		for (Field field : fields) {
			Annotation cobolItemType = getCobolItemType(field);
			if (cobolItemType == null) {
				throw new CobolConverterException("Field " + field.getName() + " in " + cobolGroup.cobolName()
						+ " does not have Cobol annotations");
			}
			CobolArray cobolArray = getCobolArray(field);
			Object value = cobolArray == null //
					? convert(cobolItemType, is, field.getType()) //
					: convertArray(cobolArray, cobolItemType, is, field.getType().getComponentType());
			setFieldValue(field, group, value);
		}
		return group;
	}

	private <Z> Z[] convertArray(CobolArray cobolArray, Annotation cobolItemType, InputStream is, Class<Z> itemClass) {
		int maxOccurs = cobolArray.dependingOn() == null //
				? cobolArray.maxOccurs() //
				: getOdoObjectValue(cobolArray.dependingOn());
		@SuppressWarnings("unchecked")
		Z[] array = (Z[]) Array.newInstance(itemClass, maxOccurs);
		for (int i = 0; i < maxOccurs; i++) {
			array[i] = convert(cobolItemType, is, itemClass);
		}
		return array;
	}

	private <Z> Z convertString(CobolString cobolString, InputStream is, Class<Z> objectClass) {
		return (Z) stringConverter.convert(is, cobolString.charNum(), objectClass);
	}

	private <Z> Z convertBinary(CobolBinary cobolBinary, InputStream is, Class<Z> objectClass) {
		Z value = (Z) binaryConverter.convert(is, cobolBinary.signed(), cobolBinary.totalDigits(), objectClass);
		if (cobolBinary.odoObject()) {
			setOdoObjectValue(cobolBinary.cobolName(), value);
		}
		return value;
	}

	private <Z> Z convertZonedDecimal(CobolZonedDecimal cobolZonedDecimal, InputStream is, Class<Z> objectClass) {
		Z value = (Z) zonedDecimalConverter.convert(is, cobolZonedDecimal.totalDigits(),
				cobolZonedDecimal.fractionDigits(), cobolZonedDecimal.signLeading(), cobolZonedDecimal.signSeparate(),
				objectClass);
		if (cobolZonedDecimal.odoObject()) {
			setOdoObjectValue(cobolZonedDecimal.cobolName(), value);
		}
		return value;
	}

	private <Z> Z convertPackedDecimal(CobolPackedDecimal cobolPackedDecimal, InputStream is, Class<Z> objectClass) {
		Z value = (Z) packedDecimalConverter.convert(is, cobolPackedDecimal.signed(), cobolPackedDecimal.totalDigits(),
				cobolPackedDecimal.fractionDigits(),
				objectClass);
		if (cobolPackedDecimal.odoObject()) {
			setOdoObjectValue(cobolPackedDecimal.cobolName(), value);
		}
		return value;
	}

	private Annotation getCobolItemType(Class<?> clazz) {
		if (clazz.isArray()) {
			return getCobolItemType(clazz.getComponentType());
		} else {
			return getCobolItemType(clazz.getDeclaredAnnotations());
		}
	}

	private Annotation getCobolItemType(Field field) {
		Annotation annotation = getCobolItemType(field.getDeclaredAnnotations());
		return annotation == null ? getCobolItemType(field.getType()) : annotation;
	}

	private CobolArray getCobolArray(Field f) {
		return f.getDeclaredAnnotation(CobolArray.class);
	}

	private Annotation getCobolItemType(Annotation[] annotations) {
		return Arrays.stream(annotations) //
				.filter(a -> a.annotationType().isAnnotationPresent(CobolItemType.class)) //
				.findFirst() //
				.orElse(null);
	}
	
	/**
	 * @param <Z>
	 * @param cobolName
	 * @param value
	 */
	private <Z> void setOdoObjectValue(String cobolName, Z value) {
		if (value instanceof Number) {
			odoObjectValues.put(cobolName, ((Number) value).intValue());
		}
	}

	private int getOdoObjectValue(String dependingOn) {
		return odoObjectValues.get(dependingOn);
	}

	/**
	 * Create a new instance of a bean.
	 * <p>
	 * Bean is assumed to have a no arg constructor.
	 */
	private <Z> Z newInstance(Class<Z> clazz) {
		try {
			Constructor<Z> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} catch (Exception e) {
			throw new CobolConverterException(e);
		}
	}

	private Object setFieldValue(Field field, Object group, Object value) {
		try {
			Method setter = group.getClass().getMethod(setterName(field), field.getType());
			return setter.invoke(group, value);
		} catch (Exception e) {
			throw new CobolConverterException(e);
		}
	}

	private Object getFieldValue(Field field, Object group) {
		try {
			Method getter = group.getClass().getMethod(getterName(field));
			return getter.invoke(group);
		} catch (Exception e) {
			throw new CobolConverterException(e);
		}
	}

	private String getterName(Field field) {
		return (field.getType().equals(Boolean.TYPE) ? "is" : "get") + Capitalize(field.getName());
	}

	private String setterName(Field field) {
		return "set" + Capitalize(field.getName());
	}

	private String Capitalize(String s) {
		return s.length() <= 1 ? s.toUpperCase() : Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
}
