package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolPackedDecimal {
	
	String cobolName();
	
	boolean signed() default false;
	
	int totalDigits();
	
	int fractionDigits() default 0;

	boolean odoObject() default false;
}
