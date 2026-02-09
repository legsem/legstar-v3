package org.legstar.cobol.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * InputStream to read RECFM=V data formats.
 * <p>
 * Data is organized into records, each starting with a 4 bytes RDW.
 */
public class RecfmVInputStream extends FilterInputStream {

	/**
	 * The current Record length.
	 * <p>
	 * -1 when expecting a RDW.
	 */
	private int recordLen = -1;

	/**
	 * Current position within the current Record.
	 */
	private int recordPos;

	/**
	 * Current position within the original input stream.
	 */
	private long pos;

	/**
	 * The input stream to be filtered.
	 */
	protected volatile InputStream in;

	public RecfmVInputStream(InputStream in) {
		super(in);
		this.in = in;
	}

	/**
	 * Read the requested number of bytes.
	 * <p>
	 * If the requested number of bytes spans beyond the current record then this
	 * translates into multiple intra-record reads until we get all the bytes
	 * requested or reach end of file.
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int flen = 0;
		while (flen < len) {
			if (primeDW()) {
				int rest = len - flen;
				int recordRest = recordLen - recordPos;
				int llen = rest <= recordRest ? rest : recordRest;
				int count = readInRecord(b, off + flen, llen);
				if (count == -1) {
					break;
				} else {
					flen += count;
				}
			} else {
				break;
			}
		}
		return flen == 0 && len > 0 ? -1 : flen;
	}

	/**
	 * Read the requested number of bytes from with a record (No DW expected within
	 * the data we read here).
	 * 
	 * @param b   where to place the bytes read
	 * @param off offset to start plaing bytes read in b
	 * @param len how many bytes should be read
	 * @return the number of bytes read. if this is smaller than len we are an end
	 *         of file
	 * @throws IOException
	 */
	private int readInRecord(byte[] b, int off, int len) throws IOException {
		byte[] lb = in.readNBytes(len);
		if (lb.length == 0) {
			return -1;
		} else {
			int aLen = lb.length;
			System.arraycopy(lb, 0, b, off, aLen);
			addToCounters(aLen);
			return aLen;
		}
	}

	@Override
	public int read() throws IOException {
		if (primeDW()) {
			int c = super.read();
			if (c == -1) {
				return -1;
			}
			addToCounters(1);
			return c;
		} else {
			return -1;
		}
	}
	
	protected void addToCounters(int count) {
		pos += count;
		recordPos += count;
	}

	/**
	 * Check the status for the current record.
	 * <p>
	 * Current record may be exhausted
	 * <p>
	 * Read record descriptor if needed
	 * 
	 * @return true if more data is available, false if at end of file
	 */
	protected boolean primeDW() throws IOException {
		if (recordPos == recordLen) {
			recordLen = -1;
		}
		if (recordLen == -1) {
			recordLen = dwLen();
			if (recordLen == -1) {
				return false;
			}
			recordPos = 0;
		}
		return true;
	}

	/**
	 * Read a 4 bytes descriptor word and extract the length from the first 2 bytes.
	 * <p>
	 * First 2 bytes contain the length including the DW length itself so we
	 * subtract that.
	 * <p>
	 * Returns -1 if EOF is reached while trying to read a DW.
	 */
	protected int dwLen() throws IOException {
		byte[] bdw = in.readNBytes(4);
		if (bdw.length < 4) {
			return -1;
		}
		pos += bdw.length;
		ByteBuffer bb = ByteBuffer.wrap(bdw);
		int len = bb.getShort();
		if (len < 4 || len > 32760 || bb.getShort() != 0) {
			throw new IOException("Descriptor word at offset " + pos + " is invalid");
		}
		return ByteBuffer.wrap(bdw).getShort() - bdw.length;
	}

	/**
	 * Mark/Reset are not supported.
	 */
	public boolean markSupported() {
		return false;
	}

	public long getPos() {
		return pos;
	}

	public int getRecordLen() {
		return recordLen;
	}

	public int getRecordPos() {
		return recordPos;
	}

}
