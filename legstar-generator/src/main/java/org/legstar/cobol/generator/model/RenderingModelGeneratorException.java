package org.legstar.cobol.generator.model;

import java.util.Stack;

import org.legstar.cobol.data.entry.CobolDataEntry;

/**
 * Denotes a rendering engine exception.
 */
public class RenderingModelGeneratorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Current group hierarchy.
	 */
	private final Stack<String> groupStack;

	/**
	 * Data entry that caused the exception
	 */
	private final CobolDataEntry dataEntry;

	/**
	 * Build exception.
	 * 
	 * @param e          the root cause
	 * @param groupStack the group stack at the moment the exception was raised
	 * @param dataEntry  the data entry being processed at the moment the exception
	 *                   was raised
	 */
	public RenderingModelGeneratorException(Throwable e, Stack<String> groupStack, CobolDataEntry dataEntry) {
		super(e);
		this.groupStack = groupStack;
		this.dataEntry = dataEntry;
	}

	/**
	 * Build exception.
	 * 
	 * @param msg        the error description
	 * @param groupStack the group stack at the moment the exception was raised
	 * @param dataEntry  the data entry being processed at the moment the exception
	 *                   was raised
	 */
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
