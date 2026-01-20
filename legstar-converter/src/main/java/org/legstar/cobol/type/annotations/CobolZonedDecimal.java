package org.legstar.cobol.type.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolItemType
public @interface CobolZonedDecimal {
	
	String cobolName();
	
	int totalDigits();
	
	int fractionDigits() default 0;

	boolean signLeading() default false;
	
	boolean signSeparate() default false;

	boolean odoObject() default false;
}
