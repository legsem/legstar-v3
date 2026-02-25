package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A cobol array.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CobolArray {

	/**
	 * Minimum number of occurrences.
	 * 
	 * @return the minimum number of occurrences
	 */
	int minOccurs();

	/**
	 * Maximum number of occurrences.
	 * 
	 * @return the maximum number of occurrences
	 */
	int maxOccurs();

	/**
	 * Preceding item giving the actual size of the array for variable size arrays
	 * 
	 * @return item giving the actual size of the array
	 */
	String dependingOn() default "";

}
