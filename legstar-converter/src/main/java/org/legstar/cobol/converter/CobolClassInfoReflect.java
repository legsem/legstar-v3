package org.legstar.cobol.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.legstar.cobol.annotation.CobolItemType;

/**
 * Provides information about a Cobol-annotated class and its fields.
 * <p>
 * Acts as a cache for methods and annotations. The intent is to reduce the
 * performance impact of reflection.
 */
public class CobolClassInfoReflect implements CobolClassInfo {

	private final Map<Integer, FieldInfo[]> fieldInfos = new ConcurrentHashMap<>();

	@Override
	public FieldInfo[] fieldInfos(Class<?> clazz) {
		return fieldInfos.computeIfAbsent(clazz.hashCode(), k -> {
			return Stream.of(clazz.getDeclaredFields()) //
					.map(this::toFieldInfo) //
					.toArray(FieldInfo[]::new);
		});
	}

	private FieldInfo toFieldInfo(Field f) {
		return new FieldInfo(f.getName(), //
				getMethod(f.getDeclaringClass(), getterName(f)), //
				Stream.of(f.getAnnotations()) //
						.filter(a -> a.annotationType().isAnnotationPresent(CobolItemType.class)) //
						.findFirst() //
						.orElse(null), //
				f.getType());
	}

	private Method getMethod(Class<?> clazz, String methodName) {
		try {
			return clazz.getMethod(methodName);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private String getterName(Field f) {
		String name = f.getName();
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
