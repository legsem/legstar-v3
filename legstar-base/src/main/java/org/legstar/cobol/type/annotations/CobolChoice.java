package org.legstar.cobol.type.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CobolItemType
public @interface CobolChoice {

	/**
	 * @return the cobol name of the first alternative in the choice (the one being redefined)
	 */
	String cobolName();

	/**
	 * @return the host data size of the largest alternative in the choice
	 */
	int maxBytesLen();

}
