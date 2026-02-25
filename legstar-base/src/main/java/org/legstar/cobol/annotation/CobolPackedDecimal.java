package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL packed decimal number (COMP-3)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolPackedDecimal {

	/**
	 * Cobol name.
	 * 
	 * @return the cobol name
	 */
	String cobolName();

	/**
	 * Signed or unsigned.
	 * 
	 * @return true if signed (false if unsigned)
	 */
	boolean signed() default false;

	/**
	 * Total number of digits (include any fraction digit)
	 * 
	 * @return total number of digits
	 */
	int totalDigits();

	/**
	 * Number of fraction digits
	 * 
	 * @return number of fraction digits
	 */
	int fractionDigits() default 0;

	/**
	 * Whether this item gives the dimension of a variable size array (Occurs
	 * Depending On)
	 * 
	 * @return true if this gives the dimension of a variable size array
	 */
	boolean odoObject() default false;
}
