package org.legstar.cobol.generator.model;

public record RenderingString(String cobolName, int charNum, RenderingArray array, String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = charNum;
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
