package org.legstar.cobol.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream to read RECFM=VB record format.
 * <p>
 * Block Descriptor Words (BDW) and Record Descriptor Words (RDW) are produced.
 */
public class RecfmVBOutputStream extends RecfmVOutputStream {

	/**
	 * A default block size (including BDW size).
	 */
	public static final int DEFAULT_BLOCK_SIZE = 32760;

	/**
	 * The block size (including BDW size).
	 */
	private final int blockSize;

	/**
	 * Current position within the block.
	 */
	private int blockPos = 0;

	/**
	 * Create a RECFM=VB output stream with default block size.
	 * 
	 * @param out the underlying output stream
	 */
	public RecfmVBOutputStream(OutputStream out) {
		this(out, DEFAULT_BLOCK_SIZE);
	}

	/**
	 * Create a RECFM=VB output stream with a specific block size.
	 * <p>
	 * Max record size is the block size minus one BDW and one RDW.
	 * 
	 * @param out       the underlying output stream
	 * @param blockSize the desired block size
	 */
	public RecfmVBOutputStream(OutputStream out, int blockSize) {
		super(out, blockSize - 8);
		this.blockSize = blockSize;
	}

	@Override
	public void write(int b) throws IOException {
		if (blockPos == 0) {
			writeDw(blockSize);
			blockPos += 4;
		}
		super.write(b);
	}

	/**
	 * When a record ends, if the record does not fit in the current block, we fill
	 * the current block with low-values and start a new block beginning with a
	 * Block Descriptor Word.
	 */
	@Override
	public void endRecord() throws IOException {
		if ((blockPos + getRecordSize()) > blockSize) {
			writeLowValues(blockSize - blockPos);
			writeDw(blockSize);
			blockPos = 4;
		} else {
			blockPos += getRecordSize();
		}
		super.endRecord();
	}

}
