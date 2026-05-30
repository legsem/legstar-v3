package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.legstar.cobol.annotation.CobolArray;

/**
 * Information about a generated Cobol annotated bean field.
 * 
 * @param name          java field name
 * @param getter        the getter method implemented by parent
 * @param setter        the setter method implemented by parent
 * @param cobolArray    the Cobol array annotation if any
 * @param cobolItemType the Cobol item type annotation if any
 * @param javaType      the field's java type
 */
public record CobolFieldInfo(String name, //
		Method getter, //
		Method setter, //
		CobolArray cobolArray, //
		Annotation cobolItemType, //
		Class<?> javaType) {

}
