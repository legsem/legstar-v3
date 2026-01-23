package org.legstar.cobol.generator.model;

/**
 * TODO unsigned native binaries may require Integer/Long/BigInteger
 */
public record RenderingBinaryNumber(String cobolName, boolean signed, int totalDigits, boolean odoObject,
		RenderingArray array) implements RenderingItem {

	public String typeName() {
		if (totalDigits <= 4) {
			return "Short";
		} else if (totalDigits <= 9) {
			return "Integer";
		} else {
			return "Long";
		}
	}
}
