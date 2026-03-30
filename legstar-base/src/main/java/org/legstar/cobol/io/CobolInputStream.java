package org.legstar.cobol.io;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A proxy on an input stream to read cobol data.
 * <p>
 * If data contains descriptor words, we filter these out with specialized input
 * streams.
 * <p>
 * Otherwise we always use a BufferedInputStream since we need mark/reset
 * semantic to support choice strategies.
 * <p>
 * Finally we need to keep track of the bytes read (for error reporting). Note
 * that at the moment this is not accurate when combined with Recfm V/VB
 * filters.
 */
public class CobolInputStream extends FilterInputStream {

	/**
	 * Number of bytes read so far.
	 */
	private long bytesRead;

	/**
	 * Number of bytes read when the mark method was called.
	 */
	private long markedBytesRead;

	/**
	 * Did we reach the end of file.
	 */
	private boolean eof;

	/**
	 * End of file condition when the mark method was called.
	 */
	private boolean markedEof;

	/**
	 * Wrap an input stream for the default fixed block record format.
	 * 
	 * @param in the input stream
	 */
	public CobolInputStream(InputStream in) {
		this(in, CobolRecordFormat.FB);
	}

	/**
	 * Wrap an input stream for the given block record format.
	 * 
	 * @param in    the input stream
	 * @param recfm the input record format
	 */
	public CobolInputStream(InputStream in, CobolRecordFormat recfm) {
		super(getBufferedInputStream(in, recfm));
	}

	@Override
	public int read() throws IOException {
		int read = super.read();
		if (read == -1) {
			eof = true;
		} else {
			bytesRead++;
		}
		return read;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int count = super.read(b, off, len);
		if (count == -1) {
			eof = true;
		} else {
			bytesRead += count;
		}
		return count;
	}

	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
		markedBytesRead = bytesRead;
		markedEof = eof;
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
		bytesRead = markedBytesRead;
		eof = markedEof;
	}

	@Override
	public long skip(long n) throws IOException {
		long skipped = super.skip(n);
		bytesRead += skipped;
		return skipped;
	}

	/**
	 * Wraps the input stream into a buffered input stream. This is needed to
	 * support the mark/reset semantic.
	 * <p>
	 * If the input data is RECFM=V or RECFM=VB we use specialized filters to handle
	 * the descriptor words that are part of the incoming data but are not relevant
	 * to the user.
	 * 
	 * @param is    the input stream
	 * @param recfm the cobol data record format
	 * @return a BufferedInputStream
	 */
	private static BufferedInputStream getBufferedInputStream(InputStream is, CobolRecordFormat recfm) {
		BufferedInputStream bis = null;
		switch (recfm) {
		case V:
			bis = new BufferedInputStream(new RecfmVInputStream(is));
			break;
		case VB:
			bis = new BufferedInputStream(new RecfmVBInputStream(is));
			break;
		default:
			if (is instanceof BufferedInputStream) {
				bis = (BufferedInputStream) is;
			} else {
				bis = new BufferedInputStream(is);
			}
		}
		return bis;
	}

	/**
	 * How many cobol bytes were consumed so far
	 * 
	 * @return number of cobol bytes consumed
	 */
	public long getBytesRead() {
		return bytesRead;
	}

	/**
	 * Has this reached the end of file.
	 * 
	 * @return true if the end of file was reached
	 */
	public boolean isEof() {
		return eof;
	}

}
