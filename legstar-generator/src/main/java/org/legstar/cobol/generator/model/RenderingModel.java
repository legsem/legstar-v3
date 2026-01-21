package org.legstar.cobol.generator.model;

import java.util.Arrays;

/**
 * TODO: Deal with name collisions if needed
 */
public record RenderingModel(String target_package_name, RenderingItem cobol_item) {

	public String className(String cobolName) {
		String[] parts = cobolName.split("-");
		StringBuilder sb = new StringBuilder();
		Arrays.stream(parts).forEach(p -> {
			sb.append(Character.toUpperCase(p.charAt(0)));
			sb.append(p.substring(1).toLowerCase());
		});
		return sb.toString();
	}

	public String fieldName(String cobolName) {
		String[] parts = cobolName.split("-");
		StringBuilder sb = new StringBuilder();
		Arrays.stream(parts).forEach(p -> {
			if (sb.isEmpty()) {
				sb.append(Character.toLowerCase(p.charAt(0)));
			} else {
				sb.append(Character.toUpperCase(p.charAt(0)));
			}
			sb.append(p.substring(1).toLowerCase());
		});
		return sb.toString();
	}

	public String getterName(String cobolName) {
		return "get" + className(cobolName);
	}

	public String setterName(String cobolName) {
		return "set" + className(cobolName);
	}

}
