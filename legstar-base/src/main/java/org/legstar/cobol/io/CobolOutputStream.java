package org.legstar.cobol.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public class CobolOutputStream extends FilterOutputStream {

	public CobolOutputStream(OutputStream out) {
		super(out);
	}

}
