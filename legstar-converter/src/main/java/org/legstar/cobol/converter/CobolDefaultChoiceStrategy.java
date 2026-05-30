package org.legstar.cobol.converter;

/**
 * Default Choice strategy (all alternatives are eligible)
 * 
 * @param <T> the java bean type of the root item
 */
public class CobolDefaultChoiceStrategy<T> implements CobolChoiceStrategy<T> {

	@Override
	public boolean choose(T root, Object choice, CobolFieldInfo alternative) {
		return true;
	}

}
