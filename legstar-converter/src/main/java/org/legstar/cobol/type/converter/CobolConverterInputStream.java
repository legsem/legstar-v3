package org.legstar.cobol.type.converter;

import java.io.BufferedInputStream;
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

	public CobolConverterInputStream(InputStream in) {
		super(in.markSupported() ? in : new BufferedInputStream(in));
	}

	@Override
	public int read() throws IOException {
		int read = super.read();
		if (read != -1) {
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
		bytesRead += n;
		return super.skip(n);
	}

	public long getBytesRead() {
		return bytesRead;
	}

}
