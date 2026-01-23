package org.legstar.cobol.generator.model;

public record RenderingArray(int minOccurs, int maxOccurs, String dependingOn) {
	
	public boolean isVariableSize() {
		return dependingOn() != null && !dependingOn().isBlank();
	}

}
