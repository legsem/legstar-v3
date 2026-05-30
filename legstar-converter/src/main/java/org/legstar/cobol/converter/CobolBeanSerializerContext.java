package org.legstar.cobol.converter;

import org.legstar.cobol.io.CobolOutputStream;

/**
 * Java to Cobol serialization mutable context.
 */
public class CobolBeanSerializerContext extends CobolBeanConverterContextBase {

	/**
	 * The Cobol output data stream
	 */
	private final CobolOutputStream cobolOutputStream;

	/**
	 * Additional bytes that need to be output as low values before anymore bytes
	 * are written.
	 */
	private long pendingLowValues;

	/**
	 * Create a serialization context.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 */
	public CobolBeanSerializerContext(CobolOutputStream cobolOutputStream) {
		this.cobolOutputStream = cobolOutputStream;
	}

	/**
	 * The Cobol output data stream
	 * 
	 * @return the Cobol output data stream
	 */
	public CobolOutputStream cobolOutputStream() {
		return cobolOutputStream;
	}

	public long bytesCounter() {
		return cobolOutputStream.getBytesWritten();
	}

	/**
	 * Number of low-value bytes to be written before next output.
	 * 
	 * @return the number of low-value bytes to be written before next output
	 */
	public long getPendingLowValues() {
		return pendingLowValues;
	}

	/**
	 * Number of low-value bytes to be written before next output.
	 * 
	 * @param pendingLowValues the number of low-value bytes to be written before
	 *       next output
	 */
	public void setPendingLowValues(long pendingLowValues) {
		this.pendingLowValues = pendingLowValues;
	}

}
