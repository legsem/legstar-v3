package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a cobol set of alternatives which are items that redefine a base
 * item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CobolItemType
public @interface CobolChoice {

	/**
	 * The cobol name to use for this choice.
	 * 
	 * @return the cobol name of the first alternative in the choice (the one being
	 *         redefined)
	 */
	String cobolName();

	/**
	 * The cobol byte length of the largest alternative in the choice.
	 * 
	 * @return the cobol byte length of the largest alternative in the choice
	 */
	int maxBytesLen();

}
