package org.legstar.cobol.type.converter;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.CobolAnnotation;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

public class CobolConverterFromHost<T> {

	private final CobolConverterString stringConverter;

	private final CobolConverterZonedDecimal zonedDecimalConverter;

	public CobolConverterFromHost(CobolConverterConfig config) {
		this.stringConverter = new CobolConverterString(config);
		this.zonedDecimalConverter = new CobolConverterZonedDecimal(config);
	}

	public T convert(InputStream is, Class<T> outputClass) {
		try {
			Annotation annotation = getCobolAnnotation(outputClass);
			return convert(annotation, is, outputClass);
		} catch (Exception e) {
			throw new FromHostException(e);
		}
	}

	private <Z> Z convert(Annotation annotation, InputStream is, Class<Z> objectClass) {
		if (annotation instanceof CobolGroup) {
			return convertGroup((CobolGroup) annotation, is, objectClass);
		} else if (annotation instanceof CobolString) {
			return convertString((CobolString) annotation, is, objectClass);
		} else if (annotation instanceof CobolZonedDecimal) {
			return convertZonedDecimal((CobolZonedDecimal) annotation, is, objectClass);
		} else {
			throw new FromHostException("Unsupported Cobol annotation " + annotation);
		}
	}

	private <Z> Z convertGroup(CobolGroup cobolGroup, InputStream is, Class<Z> groupClass) {
		Z group = newInstance(groupClass);
		Field[] fields = groupClass.getDeclaredFields();
		for (Field f : fields) {
			Annotation fieldAnnotation = getCobolAnnotation(f);
			if (fieldAnnotation == null) {
				throw new FromHostException(
						"Field " + f.getName() + " in " + cobolGroup.cobolName() + " does not have Cobol annotations");
			}
			Object value = convert(fieldAnnotation, is, f.getType());
			setFieldValue(f, group, value);
		}
		return group;
	}

	private <Z> Z convertString(CobolString cobolString, InputStream is, Class<Z> objectClass) {
		return (Z) stringConverter.convert(is, cobolString.charNum(), objectClass);
	}

	private <Z> Z convertZonedDecimal(CobolZonedDecimal cobolZonedDecimal, InputStream is, Class<Z> objectClass) {
		return (Z) zonedDecimalConverter.convert(is, cobolZonedDecimal.totalDigits(),
				cobolZonedDecimal.fractionDigits(), cobolZonedDecimal.signLeading(), cobolZonedDecimal.signSeparate(),
				objectClass);
	}

	private Annotation getCobolAnnotation(Class<?> clazz) {
		return getCobolAnnotation(clazz.getDeclaredAnnotations());
	}

	/**
	 * Primitive type fields are annotated directly. Complex types have type
	 * annotations instead.
	 */
	private Annotation getCobolAnnotation(Field field) {
		Annotation annotation = getCobolAnnotation(field.getDeclaredAnnotations());
		return annotation == null ? getCobolAnnotation(field.getType()) : annotation;
	}

	private Annotation getCobolAnnotation(Annotation[] annotations) {
		return Arrays.stream(annotations) //
				.filter(a -> a.annotationType().isAnnotationPresent(CobolAnnotation.class)) //
				.findAny() //
				.orElse(null);
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
			throw new FromHostException(e);
		}
	}

	private Object setFieldValue(Field field, Object group, Object value) {
		try {
			Method setter = group.getClass().getMethod(setterName(field), field.getType());
			return setter.invoke(group, value);
		} catch (Exception e) {
			throw new FromHostException(e);
		}
	}

	private Object getFieldValue(Field field, Object group) {
		try {
			Method getter = group.getClass().getMethod(getterName(field));
			return getter.invoke(group);
		} catch (Exception e) {
			throw new FromHostException(e);
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
