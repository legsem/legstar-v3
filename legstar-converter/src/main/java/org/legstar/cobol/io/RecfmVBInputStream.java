package org.legstar.cobol.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream to read RECFM=VB data formats.
 * <p>
 * Data is organized into blocks, each starting with a 4 bytes BDW.
 * <p>
 * Within each block, one or more records, each starting with a 4 bytes RDW.
 */
public class RecfmVBInputStream extends RecfmVInputStream {

	/**
	 * The current Block length.
	 * <p>
	 * -1 when expecting a BDW.
	 */
	private int blockLen = -1;

	/**
	 * Current position within the current Block.
	 */
	private int blockPos;

	public RecfmVBInputStream(InputStream in) {
		super(in);
	}

	@Override
	protected void addToCounters(int count) {
		super.addToCounters(count);
		blockPos += count;
	}

	/**
	 * Check the status for the current block and record.
	 * <p>
	 * Current record or block may be exhausted
	 * <p>
	 * Read block or record descriptor if needed
	 * 
	 * @return true if more data is available, false if at end of file
	 */
	@Override
	protected boolean primeDW() throws IOException {

		if (blockPos == blockLen) {
			blockLen = -1;
		}
		if (blockLen == -1) {
			blockLen = dwLen();
			if (blockLen == -1) {
				return false;
			}
			blockPos = 0;
		}

		if (super.primeDW()) {
			if (getRecordPos() == 0) {
				blockPos += 4;
			}
		} else {
			return false;
		}

		return true;
	}

	public int getBlockLen() {
		return blockLen;
	}

	public int getBlockPos() {
		return blockPos;
	}

}
