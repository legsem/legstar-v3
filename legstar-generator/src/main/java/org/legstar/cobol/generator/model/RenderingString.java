package org.legstar.cobol.generator.model;

public record RenderingString(String cobolName, int charNum, RenderingArray array)
		implements RenderingItem {
}
