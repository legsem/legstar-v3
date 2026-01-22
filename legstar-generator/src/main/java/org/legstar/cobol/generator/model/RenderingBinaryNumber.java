package org.legstar.cobol.generator.model;

/**
 * TODO native binaries need more thoughts
 */
public record RenderingBinaryNumber(String cobolName, boolean signed, int totalDigits, boolean odoObject)
		implements RenderingItem {
	
	public String typeName() {
		if (totalDigits <= 4) {
			return "short";
		} else if (totalDigits <= 9) {
			return "int";
		} else {
			return "long";
		}
	}
}
