package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL alphanumeric
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolString {

	/**
	 * Cobol name.
	 * 
	 * @return the cobol name
	 */
	String cobolName();

	/**
	 * The number of characters in this string
	 * 
	 * @return the number of characters
	 */
	int charNum();
}
