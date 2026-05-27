package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.legstar.cobol.annotation.CobolArray;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolItemType;

/**
 * Provides information about a Cobol-annotated class and its fields.
 * <p>
 * Acts as a cache for methods and annotations. The intent is to reduce the
 * performance impact of reflection.
 */
public class CobolClassInfoReflect implements CobolClassInfo {

	private final Map<Integer, CobolFieldInfo[]> fieldInfos = new ConcurrentHashMap<>();

	private final Map<Integer, Constructor<?>> constructorCache = new ConcurrentHashMap<>();

	private final Map<Integer, Annotation> cobolItemTypeCache = new ConcurrentHashMap<>();

	@Override
	public CobolFieldInfo[] fieldInfos(Class<?> clazz) {
		return fieldInfos.computeIfAbsent(clazz.hashCode(), k -> {
			return Stream.of(clazz.getDeclaredFields()) //
					.filter(this::isCobolField) //
					.map(this::toFieldInfo) //
					.toArray(CobolFieldInfo[]::new);
		});
	}

	/**
	 * Create a new instance of a class.
	 * <p>
	 * Class is assumed to have a no arg constructor.
	 * <p>
	 * Since this is expensive, we cache the constructor method.
	 * 
	 * @param <Z>   the target instance class type
	 * @param clazz the target instance class
	 * @return a new instance
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <Z> Z newInstance(Class<Z> clazz) {
		try {
			return (Z) constructorCache.computeIfAbsent(clazz.hashCode(), c -> {
				try {
					return clazz.getConstructor();
				} catch (Throwable e) {
					throw new CobolBeanConverterException(e);
				}
			}).newInstance();
		} catch (Throwable e) {
			throw new CobolBeanConverterException(e);
		}
	}

	/**
	 * Retrieve a class Cobol annotation.
	 * 
	 * @param clazz the class
	 * @return the Cobol annotation on that class or null if not found
	 */
	@Override
	public Annotation getCobolItemType(Class<?> clazz) {
		return cobolItemTypeCache.computeIfAbsent(clazz.hashCode(), c -> {
			return getCobolItemType(clazz.getAnnotations());
		});
	}

	/**
	 * A Cobol field either have a Cobol annotation or references an inner class.
	 * <p>
	 * In the case of arrays, the inner class would be that of an item.
	 * 
	 * @param field the field to check
	 * @return true if this is a Cobol field
	 */
	private boolean isCobolField(Field field) {
		return getCobolItemType(field) != null //
				|| isInnerClass(field.getDeclaringClass(), field.getType()) //
				|| (field.getType().isArray() //
						&& isInnerClass(field.getDeclaringClass(), field.getType().getComponentType()));
	}

	/**
	 * Is the child class an inner class of the parent class.
	 * 
	 * @param parent the parent class
	 * @param child  the child class
	 * @return true if the child class an inner class of the parent class
	 */
	private boolean isInnerClass(Class<?> parent, Class<?> child) {
		return parent.equals(child.getEnclosingClass());
	}

	/**
	 * Collect useful field information for the purpose of converting to and from
	 * Cobol.
	 * 
	 * @param field the field
	 * @return useful field information
	 */
	private CobolFieldInfo toFieldInfo(Field field) {
		Annotation cobolItemType = getCobolItemType(field);
		return new CobolFieldInfo(field.getName(), //
				getMethod(field.getDeclaringClass(), getterName(field)), //
				getCobolArray(field), //
				cobolItemType, //
				field.getType(), //
				isAlternative(field));
	}

	/**
	 * If this an alternative in choice.
	 * 
	 * @param field the field to check
	 * @return true if the field's parent is a Cobol choice
	 */
	private boolean isAlternative(Field field) {
		Class<?> parent = field.getDeclaringClass();
		Annotation cobolAnnotation = getCobolItemType(parent);
		return cobolAnnotation instanceof CobolChoice;
	}

	/**
	 * Retrieve a field Cobol annotation.
	 * 
	 * @param field the field
	 * @return the Cobol annotation on that field or null if not found
	 */
	private Annotation getCobolItemType(Field field) {
		Annotation annotation = getCobolItemType(field.getAnnotations());
		if (annotation == null) {
			if (field.getType().isArray()) {
				return getCobolItemType(field.getType().componentType());
			} else {
				return getCobolItemType(field.getType());
			}
		} else {
			return annotation;
		}
	}

	/**
	 * Retrieve the cobol array annotation.
	 * 
	 * @param field the child
	 * @return the cobol array annotation or null if not found
	 */
	private CobolArray getCobolArray(Field field) {
		return field.getDeclaredAnnotation(CobolArray.class);
	}

	/**
	 * Retrieve the cobol annotation among all annotations.
	 * 
	 * @param annotations a collection of annotations
	 * @return the Cobol annotation or null if not found
	 */
	private Annotation getCobolItemType(Annotation[] annotations) {
		return Stream.of(annotations) //
				.filter(a -> a.annotationType().isAnnotationPresent(CobolItemType.class)) //
				.findFirst() //
				.orElse(null);
	}

	/**
	 * Method corresponding to method name.
	 * <p>
	 * The method name must exist in the parameter class.
	 * 
	 * @param clazz      the class implementing the requested method
	 * @param methodName the requested method name
	 * @return the method or throws a runtime exception if the method does not exist
	 *         or cannot be accessed
	 */
	private Method getMethod(Class<?> clazz, String methodName) {
		try {
			return clazz.getMethod(methodName);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Getter method name for the given field.
	 * <p>
	 * This assumes the bean was generated by legstar and therefore the getter name
	 * follows a strict set of rules.
	 * 
	 * @param f the field
	 * @return the getter method name for that field
	 */
	private String getterName(Field f) {
		String name = f.getName();
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	

}
