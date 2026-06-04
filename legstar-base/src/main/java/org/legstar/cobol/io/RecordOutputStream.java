package org.legstar.cobol.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A record-aware FilterOutputStream.
 * <p>
 * Allows for special processing when a record is being written out.
 */
public abstract class RecordOutputStream extends FilterOutputStream {

	/**
	 * Build a record-aware output stream
	 * 
	 * @param out the underlying output stream
	 */
	public RecordOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * A complete record is available and may need post processing.
	 * <p>
	 * The record may be buffered at this stage and may need to be actually written
	 * to the underlying output stream.
	 * 
	 * @throws IOException if record post processing fails to write the record
	 */
	public abstract void endRecord() throws IOException;

}
