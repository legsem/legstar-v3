package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL float number (COMP-1)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolFloat {

	/**
	 * Cobol name.
	 * 
	 * @return the cobol name
	 */
	String cobolName();

}
