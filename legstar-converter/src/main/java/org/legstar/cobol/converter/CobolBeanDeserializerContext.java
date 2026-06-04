package org.legstar.cobol.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.legstar.cobol.io.CobolInputStream;

/**
 * Cobol to Java de-serialization mutable context.
 */
public class CobolBeanDeserializerContext extends CobolBeanConverterContextBase {

	/**
	 * The Cobol input data stream
	 */
	private final CobolInputStream cobolInputStream;
	
	/**
	 * The group hierarchy that is traversed
	 */
	private final Stack<Object> groupStack = new Stack<>();

	/**
	 * The variable size array dimensions collected
	 */
	private final Map<String, Integer> odoObjectValues = new HashMap<>();
	
	/**
	 * Create a de-serializer context
	 * 
	 * @param cobolInputStream the Cobol input data stream
	 */
	public CobolBeanDeserializerContext(CobolInputStream cobolInputStream) {
		this.cobolInputStream = cobolInputStream;
	}

	@Override
	public long bytesCounter() {
		return cobolInputStream.getBytesRead();
	}

	/**
	 * The Cobol input data stream
	 * 
	 * @return the Cobol input data stream
	 */
	public CobolInputStream cobolInputStream() {
		return cobolInputStream;
	}

	/**
	 * How many Cobol bytes were read so far.
	 * 
	 * @return the current cobol input byte count
	 */
	public long getBytesRead() {
		return cobolInputStream.getBytesRead();
	}

	/**
	 * Reset the cobol data input to the previously marked position.
	 * 
	 * @throws IOException if resetting fails
	 */
	public void reset() throws IOException {
		cobolInputStream.reset();
	}

	/**
	 * Mark the position in the cobol input data so that we can eventually reset to
	 * that position.
	 * 
	 * @param maxBytesLen the maximum limit of bytes that can be read before the
	 *                    mark position becomes invalid.
	 */
	public void mark(int maxBytesLen) {
		cobolInputStream.mark(maxBytesLen);
	}

	/**
	 * Skip a number of bytes in the cobol input data.
	 * 
	 * @param leftover the number of bytes to skip
	 * @return the number of bytes actually skipped
	 * @throws IOException if skipping fails
	 */
	public long skip(long leftover) throws IOException {
		return cobolInputStream.skip(leftover);
	}

	/**
	 * Push a group on the stack.
	 * 
	 * @param group to push
	 */
	public void pushGroup(Object group) {
		groupStack.push(group);
	}

	/**
	 * Pop a group from the stack.
	 */
	public void popGroup() {
		groupStack.pop();
	}

	/**
	 * Get the root item.
	 * 
	 * @param <Z> the root item type
	 * @return the root item or null if no item was converted yet
	 */
	@SuppressWarnings("unchecked")
	<Z> Z getRoot() {
		return groupStack.isEmpty() ? null : (Z) groupStack.get(0);
	}

	/**
	 * Given a value, set the corresponding variable size Cobol array size.
	 * 
	 * @param cobolName Cobol name of the variable giving the array size (Occurs
	 *                  depending on object).
	 * @param value     the value
	 */
	public void putOdoObjectValue(String cobolName, int value) {
		odoObjectValues.put(cobolName, value);
	}

	/**
	 * Retrieve the actual size of a variable size Cobol array.
	 * 
	 * @param cobolName Cobol name of the variable giving the array size (Occurs
	 *                  depending on object).
	 * @return the current size of the array
	 */
	public Integer getOdoObjectValue(String cobolName) {
		return odoObjectValues.get(cobolName);
	}

}
