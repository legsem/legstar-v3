package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * COBOL zoned decimal number
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolZonedDecimal {

	/**
	 * Cobol name.
	 * 
	 * @return the cobol name
	 */
	String cobolName();

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
	 * Signed or unsigned.
	 * 
	 * @return true if signed (false if unsigned)
	 */
	boolean signed() default false;

	/**
	 * Is the sign leading (trailing otherwise)
	 * 
	 * @return true if the sign is leading
	 */
	boolean signLeading() default false;

	/**
	 * Is the sign separate (overpunched otherwise)
	 * 
	 * @return true if the sign is sparate
	 */
	boolean signSeparate() default false;

	/**
	 * Is the content blank instead of zeroes
	 * 
	 * @return true if the content blank instead of zeroes
	 */
	boolean blankWhenZero() default false;

	/**
	 * Whether this item gives the dimension of a variable size array (Occurs
	 * Depending On)
	 * 
	 * @return true if this gives the dimension of a variable size array
	 */
	boolean odoObject() default false;
}
