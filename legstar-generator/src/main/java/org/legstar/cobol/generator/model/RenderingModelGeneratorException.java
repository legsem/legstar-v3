package org.legstar.cobol.generator.model;

import java.util.Stack;

import org.legstar.cobol.data.entry.CobolDataEntry;

public class RenderingModelGeneratorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final Stack<String> groupStack;
	
	private final CobolDataEntry dataEntry;
	
	public RenderingModelGeneratorException(Throwable e, Stack<String> groupStack, CobolDataEntry dataEntry) {
		super(e);
		this.groupStack = groupStack;
		this.dataEntry = dataEntry;
	}

	public RenderingModelGeneratorException(String msg, Stack<String> groupStack, CobolDataEntry dataEntry) {
		super(msg);
		this.groupStack = groupStack;
		this.dataEntry = dataEntry;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage());
		if (groupStack != null && !groupStack.isEmpty()) {
			String qualifiedGroup = String.join(".", groupStack);
			sb.append(" {");
			sb.append("Cobol group: '");
			sb.append(qualifiedGroup);
			sb.append("'");
			if (dataEntry == null) {
				sb.append("}");
			}
		}
		if (dataEntry != null) {
			if (groupStack == null || groupStack.isEmpty()) {
				sb.append(" {");
			} else {
				sb.append(", ");
			}
			sb.append("Cobol item: '");
			sb.append(dataEntry.cobolName());
			sb.append("'");
			sb.append(", ");
			sb.append("copybook line: ");
			sb.append(dataEntry.srceLine());
			sb.append("}");
		}
		return sb.toString();
	}

}
