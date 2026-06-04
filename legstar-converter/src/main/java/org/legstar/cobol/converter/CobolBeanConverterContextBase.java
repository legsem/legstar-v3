package org.legstar.cobol.converter;

import java.lang.annotation.Annotation;
import java.util.Stack;

import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolZonedDecimal;

/**
 * Common converter mutable context shared by serializer and de-serializer.
 */
public abstract class CobolBeanConverterContextBase {

	/**
	 * The Cobol annotation hierarchy that is traversed
	 */
	private final Stack<Annotation> cobolItemTypeStack;

	/**
	 * Create a bean converter context.
	 */
	public CobolBeanConverterContextBase() {
		cobolItemTypeStack = new Stack<>();
	}

	/**
	 * Push a Cobol annotation on the stack.
	 * 
	 * @param annotation the Cobol annotation to push
	 */
	public void pushCobolItemType(Annotation annotation) {
		cobolItemTypeStack.push(annotation);
	}

	/**
	 * Pop a Cobol annotation from the stack.
	 */
	public void popCobolItemType() {
		cobolItemTypeStack.pop();
	}

	/**
	 * Number of Cobol bytes already converted.
	 * 
	 * @return the number of Cobol bytes already converted
	 */
	public abstract long bytesCounter();

	/**
	 * Create a qualified cobol name.
	 * 
	 * @return qualified cobol name starting at the root
	 */
	public String cobolQualifiedName() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cobolItemTypeStack.size(); i++) {
			if (i > 0) {
				sb.append(".");
			}
			sb.append(cobolName(cobolItemTypeStack.get(i)));
		}
		return sb.toString();
	}

	/**
	 * Given a Cobol annotation, retrieve the Cobol item's name.
	 * 
	 * @param annotation
	 * @return the Cobol name or null if none is found
	 */
	private String cobolName(Annotation annotation) {
		if (annotation instanceof CobolGroup) {
			return ((CobolGroup) annotation).cobolName();
		} else if (annotation instanceof CobolChoice) {
			return ((CobolChoice) annotation).cobolName();
		} else if (annotation instanceof CobolBinaryNumber) {
			return ((CobolBinaryNumber) annotation).cobolName();
		} else if (annotation instanceof CobolZonedDecimal) {
			return ((CobolZonedDecimal) annotation).cobolName();
		} else if (annotation instanceof CobolPackedDecimal) {
			return ((CobolPackedDecimal) annotation).cobolName();
		} else if (annotation instanceof CobolFloat) {
			return ((CobolFloat) annotation).cobolName();
		} else if (annotation instanceof CobolDouble) {
			return ((CobolDouble) annotation).cobolName();
		} else {
			return null;
		}
	}

}
