package org.legstar.cobol.generator.model;

import java.util.List;

/**
 * Describes a cobol set of alternatives which are items that redefine a base
 * item.
 * 
 * @param cobolName    cobol name
 * @param alternatives the ordered collection of alternatives
 * @param array        If this is an array, null otherwise
 * @param fieldName    java name
 */
public record RenderingChoice(String cobolName, List<RenderingItem> alternatives, RenderingArray array,
		String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = alternatives.stream() //
				.mapToInt(RenderingItem::maxBytesLen) //
				.max() //
				.getAsInt();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}

}
