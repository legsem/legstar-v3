package org.legstar.cobol.generator.model;

public interface RenderingItem {

	String cobolName();

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

}
