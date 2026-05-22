package org.legstar.cobol.converter;

/**
 * Information about a generated Cobol annotated bean class.
 */
public interface CobolClassInfo {

	/**
	 * Returns field informations for a cobol annotated class.
	 * 
	 * @param clazz the class to introspect
	 * @return the field informations
	 */
	CobolFieldInfo[] fieldInfos(Class<?> clazz);
	
	/**
	 * Create a new instance of a cobol-annotated class.
	 * 
	 * @param <Z> the class type
	 * @param clazz the cobol-annotated class
	 * @return a new instance
	 */
	<Z> Z newInstance(Class<Z> clazz);


}
