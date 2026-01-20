package org.legstar.cobol.type.converter;

import java.lang.reflect.Field;

public interface CobolConverterFromHostChoiceStrategy<T> {

	/**
	 * Given an alternative field in a choice, determine if this is the right
	 * alternative.
	 * 
	 * @param root        Root object. It will hold values converted so far. This is
	 *                    the top level ancestor of the Choice being converted.
	 * @param alternative the alternative field to check as the chosen alternative
	 *                    in the choice
	 * @return true if this field is the right choice
	 */
	 boolean choose(T root, Field alternative);

}
