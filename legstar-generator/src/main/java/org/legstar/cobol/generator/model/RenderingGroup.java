package org.legstar.cobol.generator.model;

import java.util.List;

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
