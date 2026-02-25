package org.legstar.cobol.generator.model;

import java.util.List;

/**
 * Describes a cobol group item
 * 
 * @param cobolName cobol name
 * @param fields    the ordered collection of children
 * @param array     If this is an array, null otherwise
 * @param fieldName java name
 */
public record RenderingGroup(String cobolName, List<RenderingItem> fields, RenderingArray array, String fieldName)
		implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = fields.stream() //
				.mapToInt(RenderingItem::maxBytesLen) //
				.sum();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}

}
