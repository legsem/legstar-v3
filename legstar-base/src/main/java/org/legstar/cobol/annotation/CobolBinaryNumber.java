package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL binary number (COMP, COMP-5)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolBinaryNumber {

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
	 * total number of digits (include any fraction digit)
	 * 
	 * @return total number of digits
	 */
	int totalDigits();

	/**
	 * Whether this item gives the dimension of a variable size array (Occurs
	 * Depending On)
	 * 
	 * @return true if this gives the dimension of a variable size array
	 */
	boolean odoObject() default false;
}
