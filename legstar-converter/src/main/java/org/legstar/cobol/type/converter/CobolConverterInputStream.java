package org.legstar.cobol.type.converter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple proxy on an input stream that allow tracking how may bytes were
 * read.
 * <p>
 * This is useful for error reporting
 */
public class CobolConverterInputStream extends FilterInputStream {

	private long bytesRead;

	private long markedBytesRead;

	protected CobolConverterInputStream(InputStream in) {
		super(in);
		if (!in.markSupported()) {
			throw new IllegalArgumentException("The input stream class " + in.getClass() + " does not support marking");
		}
	}

	@Override
	public int read() throws IOException {
		int read = super.read();
		if (read > 0) {
			bytesRead++;
		}
		return read;
	}

	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
		markedBytesRead = bytesRead;
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
		bytesRead = markedBytesRead;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int count = super.read(b, off, len);
		if (count != -1) {
			bytesRead += count;
		}
		return count;
	}

	@Override
	public long skip(long n) throws IOException {
		throw new UnsupportedOperationException();
	}

	public long getBytesRead() {
		return bytesRead;
	}

}
