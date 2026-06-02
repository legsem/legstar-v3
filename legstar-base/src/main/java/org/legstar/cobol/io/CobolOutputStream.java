package org.legstar.cobol.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * On Cobol output, we may have to insert Record Descriptor Words to comply with
 * the RECFM=V or RECFM=VB formats.
 * <p>
 * We also systematically use a BufferedOutputStream for efficiency.
 */
public class CobolOutputStream extends RecordOutputStream {

	/**
	 * How many bytes were actually written.
	 */
	private long bytesWritten;

	/**
	 * Wrap an output stream for the default fixed block record format.
	 * 
	 * @param os the output stream
	 */
	public CobolOutputStream(OutputStream os) {
		this(os, CobolRecordFormat.FB);
	}

	/**
	 * Wrap an output stream for the given block record format.
	 * 
	 * @param os    the output stream
	 * @param recfm the output record format
	 */
	public CobolOutputStream(OutputStream os, CobolRecordFormat recfm) {
		super(recordOutputStream(bufferedOutputStream(os), recfm));
	}

	/**
	 * How many cobol bytes were written so far
	 * 
	 * @return number of cobol bytes written
	 */
	public long getBytesWritten() {
		return bytesWritten;
	}

	@Override
	public void write(int b) throws IOException {
		super.write(b);
		bytesWritten += 1;
	}

	@Override
	public void endRecord() throws IOException {
		((RecordOutputStream) out).endRecord();
	}

	/**
	 * Wraps an output stream as a record-aware output stream.
	 * 
	 * @param os    the underlying output stream
	 * @param recfm the cobol record format
	 * @return a record-aware output stream
	 */
	private static RecordOutputStream recordOutputStream(OutputStream os, CobolRecordFormat recfm) {
		switch (recfm) {
		case V:
			return new RecfmVOutputStream(os);
		case VB:
			return new RecfmVBOutputStream(os);
		default:
			return new RecfmFBOutputStream(os);
		}
	}

	/**
	 * Wraps an output stream into a buffered output stream if needed.
	 * 
	 * @param os the underlying output stream
	 * @return a BufferedOutputStream
	 */
	private static BufferedOutputStream bufferedOutputStream(OutputStream os) {
		return os instanceof BufferedOutputStream ? (BufferedOutputStream) os : new BufferedOutputStream(os);
	}

}
