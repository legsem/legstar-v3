package org.legstar.cobol.generator.model;

import java.util.List;

public record RenderingChoice(List<RenderingItem> alternatives, RenderingArray array) implements RenderingItem {

	/**
	 * Choices are not materialized in cobol and therefore don't have an explicit
	 * cobol name. Here we derive a cobol name from the first alternative (the one
	 * being redefined).
	 */
	@Override
	public String cobolName() {
		return (alternatives.isEmpty() ? null : alternatives.get(0).cobolName()) + "-CHOICE";
	}
	
	@Override
	public int maxBytesLen() {
		int maxBytesLen = alternatives.stream() //
				.mapToInt(RenderingItem::maxBytesLen) //
				.max() //
				.getAsInt();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}

}
