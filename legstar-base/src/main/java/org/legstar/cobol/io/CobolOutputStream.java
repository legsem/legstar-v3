package org.legstar.cobol.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CobolOutputStream extends FilterOutputStream {

	private long bytesWritten;

	public CobolOutputStream(OutputStream out) {
		super(out);
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
	public void write(byte[] b) throws IOException {
		super.write(b);
		bytesWritten += b.length;
	}


}
