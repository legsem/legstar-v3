package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL double number (COMP-2)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolDouble {

	/**
	 * Cobol name.
	 * 
	 * @return the cobol name
	 */
	String cobolName();

}
