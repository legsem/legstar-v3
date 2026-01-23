package org.legstar.cobol.generator.model;

import java.util.List;

public record RenderingGroup(String cobolName, List<RenderingItem> fields, RenderingArray array)
		implements RenderingItem {

}
