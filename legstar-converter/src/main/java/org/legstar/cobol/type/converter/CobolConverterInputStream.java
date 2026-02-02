package org.legstar.cobol.type.converter;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.legstar.cobol.io.RecfmVBInputStream;
import org.legstar.cobol.io.RecfmVInputStream;

/**
 * A proxy on an input stream to read host data.
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
public class CobolConverterInputStream extends FilterInputStream {

	private long bytesRead;

	private long markedBytesRead;

	public CobolConverterInputStream(InputStream in) {
		this(in, HostDataRecordFormat.FB);
	}

	public CobolConverterInputStream(InputStream in, HostDataRecordFormat recfm) {
		super(getBufferedInputStream(in, recfm));
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

	private static BufferedInputStream getBufferedInputStream(InputStream is, HostDataRecordFormat recfm) {
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

}
