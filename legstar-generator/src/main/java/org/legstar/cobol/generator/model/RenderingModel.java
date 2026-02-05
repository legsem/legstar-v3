package org.legstar.cobol.generator.model;

public record RenderingModel(String package_name, RenderingItem root_item, boolean withToString, RenderingOptions options) {
}
