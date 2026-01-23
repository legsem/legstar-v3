package org.legstar.cobol.generator.model;

public interface RenderingItem {
	
	default boolean isArray() {
		return array() != null;
	}
	
	default RenderingArray array() {
		return null;
	}

}
