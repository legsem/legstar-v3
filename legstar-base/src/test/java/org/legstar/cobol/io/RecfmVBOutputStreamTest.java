package org.legstar.cobol.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class RecfmVBOutputStreamTest {
	
	@Test
	public void testSingleRecord() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVBOutputStream out = new RecfmVBOutputStream(baos, 12);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		assertEquals("000c000000060000c1c2", HexFormat.of().formatHex(baos.toByteArray()));
	}

	@Test
	public void testTwoRecords() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVBOutputStream out = new RecfmVBOutputStream(baos, 16);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("C3"));
		out.endRecord();
		assertEquals("0010000000060000c1c200050000c3", HexFormat.of().formatHex(baos.toByteArray()));
	}

	@Test
	public void testTwoRecordsExact() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVBOutputStream out = new RecfmVBOutputStream(baos, 15);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("C3"));
		out.endRecord();
		assertEquals("000f000000060000c1c200050000c3", HexFormat.of().formatHex(baos.toByteArray()));
	}

	@Test
	public void testTwoRecordsTwoBlocks() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVBOutputStream out = new RecfmVBOutputStream(baos, 14);
		out.write(HexFormat.of().parseHex("C1C2"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("C3"));
		out.endRecord();
		assertEquals("000e000000060000c1c200000000000e000000050000c3", HexFormat.of().formatHex(baos.toByteArray()));
	}

	@Test
	public void testThreeRecordsTwoBlocks() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RecfmVBOutputStream out = new RecfmVBOutputStream(baos, 14);
		out.write(HexFormat.of().parseHex("C1"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("C2"));
		out.endRecord();
		out.write(HexFormat.of().parseHex("C4C5"));
		out.endRecord();
		assertEquals("000e000000050000c100050000c2000e000000060000c4c5", HexFormat.of().formatHex(baos.toByteArray()));
	}
}
