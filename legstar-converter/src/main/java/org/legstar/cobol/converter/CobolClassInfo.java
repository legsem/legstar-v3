package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;

/**
 * Information about a generated Cobol annotated bean class.
 */
public interface CobolClassInfo {

	/**
	 * Returns field informations for a Cobol annotated class.
	 * 
	 * @param clazz the class to introspect
	 * @return the field informations
	 */
	CobolFieldInfo[] fieldInfos(Class<?> clazz);
	
	/**
	 * Create a new instance of a Cobol annotated class.
	 * 
	 * @param <Z> the class type
	 * @param clazz the cobol-annotated class
	 * @return a new instance
	 */
	<Z> Z newInstance(Class<Z> clazz);
	
	/**
	 * Retrieve the Cobol annotation on a class.
	 * 
	 * @param clazz the java class
	 * @return the Cobol annotation or null if none is found
	 */
	Annotation getCobolItemType(Class<?> clazz);


}
