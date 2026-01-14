package org.legstar.cobol.type.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolAnnotation
public @interface CobolPackedDecimal {
	
	String cobolName();
	
	boolean signed();
	
	int totalDigits();
	
	int fractionDigits();

}
