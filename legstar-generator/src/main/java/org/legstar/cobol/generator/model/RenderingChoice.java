package org.legstar.cobol.generator.model;

import java.util.List;

public record RenderingChoice(String cobolName, List<RenderingItem> alternatives, RenderingArray array, String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = alternatives.stream() //
				.mapToInt(RenderingItem::maxBytesLen) //
				.max() //
				.getAsInt();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
	
}
