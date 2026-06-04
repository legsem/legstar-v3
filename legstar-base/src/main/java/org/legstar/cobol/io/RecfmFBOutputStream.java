package org.legstar.cobol.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream to read RECFM=FB (default) record format.
 * <p>
 * No descriptor words are produced in this case.
 */
public class RecfmFBOutputStream extends RecordOutputStream {

	/**
	 * Build a RECFM=FB output stream
	 * 
	 * @param out the underlying output stream
	 */
	public RecfmFBOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void endRecord() throws IOException {
		// No particular processing needed
	}

}
