package org.legstar.cobol.generator.model;

public interface RenderingItem {

	/**
	 * A unique java field name for this item with its parent group or choice
	 */
	String fieldName();

	/**
	 * @return the maximum number of bytes this item occupies on the host
	 */
	int maxBytesLen();

	default boolean isArray() {
		return array() != null;
	}

	default RenderingArray array() {
		return null;
	}

	default String className() {
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(fieldName().charAt(0)));
		sb.append(fieldName().substring(1));
		return sb.toString();
	}

	default String getterName() {
		return "get" + className();
	}

	default String setterName() {
		return "set" + className();
	}


}
