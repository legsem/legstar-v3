package org.legstar.cobol.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class RecfmVOutputStreamTest {
	
	@Test
	public void testWriteDw() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVOutputStream out = new RecfmVOutputStream(baos);
		out.writeDw(32760);
		assertEquals("7ff80000", HexFormat.of().formatHex(baos.toByteArray()));
	}
	
	@Test
	public void testWriteOneRecord() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVOutputStream out = new RecfmVOutputStream(baos);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		assertEquals("00060000c1c2", HexFormat.of().formatHex(baos.toByteArray()));
	}

	@Test
	public void testWriteTwoRecords() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVOutputStream out = new RecfmVOutputStream(baos);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("D1D2D3"));
		out.endRecord();
		assertEquals("00060000c1c200070000d1d2d3", HexFormat.of().formatHex(baos.toByteArray()));
	}
}
