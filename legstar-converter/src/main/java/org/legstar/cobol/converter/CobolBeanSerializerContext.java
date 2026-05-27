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
import org.legstar.cobol.io.CobolOutputStream;

/**
 * Anything that is mutable (apart from caches) is stored in this context in
 * order to keep converters thread safe.
 * 
 * @param cobolOutputStream the cobol output data
 * @param groupStack       the group hierarchy that is traversed
 */
public class CobolBeanSerializerContext {
	
	/**
	 * The cobol output data
	 */
	private final CobolOutputStream cobolOutputStream;
	
	/**
	 * The Cobol annotation hierarchy that is traversed
	 */
	private final Stack<Annotation> cobolItemTypeStack = new Stack<>();
	
	public CobolBeanSerializerContext(CobolOutputStream cobolOutputStream) {
		this.cobolOutputStream = cobolOutputStream;
	}

	/**
	 * Push a Cobol annotation the stack.
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
	 * Create a qualified cobol name.
	 * 
	 * @return qualified cobol name starting at the root
	 */
	/**
	 * @return
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

	public CobolOutputStream cobolOutputStream() {
		return cobolOutputStream;
	}

	public long bytesCounter() {
		return cobolOutputStream.getBytesWritten();
	}
	
	/**
	 * Given a cobol annotation, retrieve the cobol item's name.
	 * 
	 * @param annotation
	 * @return the cobol name or null if none is found
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
