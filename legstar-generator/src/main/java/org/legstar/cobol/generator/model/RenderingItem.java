package org.legstar.cobol.generator.model;

/**
 * Generic properties of a cobol data item.
 */
public interface RenderingItem {

	/**
	 * A unique java field name for this item with its parent group or choice
	 * 
	 * @return the java name.
	 */
	String fieldName();

	/**
	 * The maximum number of bytes this item occupies in the cobol data
	 * 
	 * @return the maximum number of bytes this item occupies in the cobol data
	 */
	int maxBytesLen();

	/**
	 * Is this an array.
	 * 
	 * @return true if this item is an array
	 */
	default boolean isArray() {
		return array() != null;
	}

	/**
	 * Array properties for this item.
	 * 
	 * @return the array properties or null if this is not an array
	 */
	default RenderingArray array() {
		return null;
	}

	/**
	 * The corresponding java class name for group items.
	 * 
	 * @return a valid java class name
	 */
	default String className() {
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(fieldName().charAt(0)));
		sb.append(fieldName().substring(1));
		return sb.toString();
	}

	/**
	 * A java bean getter name for this item.
	 * 
	 * @return a java bean getter name
	 */
	default String getterName() {
		return "get" + className();
	}

	/**
	 * A java bean setter name for this item.
	 * 
	 * @return a java bean setter name
	 */
	default String setterName() {
		return "set" + className();
	}

}
