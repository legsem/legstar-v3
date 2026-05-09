package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Information about a generated Cobol annotated bean class.
 */
public interface CobolClassInfo {

	/**
	 * Returns field informations of null if this class has no fields.
	 * 
	 * @param clazz the class to introspect
	 * @return the field informations of null if this class has no fields
	 */
	FieldInfo[] fieldInfos(Class<?> clazz);

	/**
	 * Information about a generated Cobol annotated bean field.
	 */
	record FieldInfo(String name, Method getter, Annotation cobolItemType, Class<?> javaType) {
	}


}
