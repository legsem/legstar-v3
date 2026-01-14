package org.legstar.cobol.type.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CobolAnnotation
public @interface CobolBinary {

	String cobolName();

	int totalDigits();

	long minInclusive();

	long maxInclusive();

	boolean odoObject();
}
