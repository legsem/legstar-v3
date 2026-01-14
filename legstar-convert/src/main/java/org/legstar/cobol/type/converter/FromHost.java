package org.legstar.cobol.type.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.CobolAnnotation;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

public class FromHost<T> {

	public T convert(byte[] payload, T output) {
		return convert(payload, 0, payload.length, output);
	}

	public T convert(byte[] payload, int start, int len, T output) {
		if (payload.length == 0) {
			return output;
		}
		Annotation annotation = getCobolAnnotation(output.getClass());
		dispatch(annotation, payload, start, len, output);
		return output;
	}
	
	private void dispatch(Annotation annotation, byte[] payload, int start, int len, Object o) {
		if (annotation instanceof CobolGroup) {
			convert((CobolGroup) annotation, payload, start, len, o);
		} else if (annotation instanceof CobolString) {
			convert((CobolString) annotation, payload, start, len, o);
		}
	}
	
	private void convert(CobolGroup cobolGroup, byte[] payload, int start, int len, Object group) {
		
		Field[] fields = group.getClass().getDeclaredFields();
		for (Field f : fields) {
			Annotation fieldAnnotation = getCobolAnnotation(f);
			if (fieldAnnotation == null) {
				throw new FromHostException("Field " + f.getName() + " in " + cobolGroup.cobolName() + " does not have a Cobol annotation");
			}
			dispatch(fieldAnnotation, payload, start, len, getFieldValue(f, group));
		}
		
	}
	
	private Object getFieldValue(Field field, Object group) {
		try {
			return field.get(group);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new FromHostException(e);
		}
	}
	
	private void convert(CobolString cobolString, byte[] payload, int start, int len, Object o) {
		
	}
	
	private void convert(CobolZonedDecimal cobolZonedDecimal, byte[] payload, int start, int len, Object o) {
		
	}
	
	private Annotation getCobolAnnotation(Class<?> clazz) {
		return getCobolAnnotation(clazz.getDeclaredAnnotations());
	}

	/**
	 * Primitive type fields are annotated directly. Complex types have type annotations instead.
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
}
