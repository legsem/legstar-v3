package org.legstar.cobol.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * OutputStream to read RECFM=V record format.
 * <p>
 * Record Descriptor Words (RDW) are produced.
 */
public class RecfmVOutputStream extends RecordOutputStream {

	/**
	 * A reasonable maximum is needed so that we can always buffer a record.
	 */
	private static final int MAX_RECORD_SIZE = RecfmVBOutputStream.DEFAULT_BLOCK_SIZE - 8;

	/**
	 * The underlying output stream.
	 */
	private final OutputStream out;

	/**
	 * Buffer holding record while record size is unknown.
	 */
	private byte[] buffer;

	/**
	 * Current position with the buffer.
	 */
	private int pos = 0;

	/**
	 * Create a RECFM=V output stream with default maximum record size
	 * 
	 * @param out the underlying output stream
	 */
	public RecfmVOutputStream(OutputStream out) {
		this(out, MAX_RECORD_SIZE);
	}

	/**
	 * Create a RECFM=V output stream with a specific maximum record size
	 * 
	 * @param out           the underlying output stream
	 * @param maxRecordSize the maximum record size
	 */
	public RecfmVOutputStream(OutputStream out, int maxRecordSize) {
		super(out);
		this.out = out;
		buffer = new byte[maxRecordSize];
	}

	@Override
	public void write(int b) throws IOException {
		if (pos >= buffer.length) {
			throw new IOException("Record cannot be larger than " + buffer.length);
		}
		buffer[pos] = (byte) b;
		pos += 1;
	}

	/**
	 * Records must be terminated with an endRecord.
	 * <p>
	 * This is when we can calculate the record actual record size and produce a
	 * record descriptor word.
	 * <p>
	 * Beware that record size in Record Descriptor Word must include the RDW 4 bytes size.
	 * 
	 * @throws IOException if writing to the underlying stream fails
	 */
	public void endRecord() throws IOException {
		writeDw(pos + 4);
		out.write(buffer, 0, pos);
		pos = 0;
	}

	/**
	 * Write a descriptor word to the underlying stream.
	 * 
	 * @param len the length that should appear on the descriptor word
	 * @throws IOException if writing to the underlying stream fails
	 */
	protected void writeDw(int len) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putShort((short) len);
		out.write(bb.array());
	}

	/**
	 * Write low values to underlying stream.
	 * 
	 * @param len number of low-values to write
	 * @throws IOException if writing to the underlying stream fails
	 */
	protected void writeLowValues(int len) throws IOException {
		for (int i = 0; i < len; i++) {
			out.write(0);
		}
	}

	/**
	 * The record size is actual data in the buffer plus the Record Descriptor Word
	 * length.
	 * 
	 * @return the record total size
	 */
	public int getRecordSize() {
		return pos + 4;
	}

}
