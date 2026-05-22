package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.legstar.cobol.annotation.CobolArray;

/**
 * Information about a generated Cobol annotated bean field.
 */
public record CobolFieldInfo(String name, Method getter, CobolArray cobolArray, Annotation cobolItemType, Class<?> javaType,
		boolean isAlternative) {

}
