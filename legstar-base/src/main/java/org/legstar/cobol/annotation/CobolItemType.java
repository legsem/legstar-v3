package org.legstar.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A COBOL item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.ANNOTATION_TYPE})
public @interface CobolItemType {

}
