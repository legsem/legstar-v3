package org.legstar.cobol.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class RecfmVBInputStreamTest {

	@Test
	public void testReadOneBlockOneRecord() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("0009000000050000C1"));
		RecfmVBInputStream vbis = new RecfmVBInputStream(is);
		assertEquals(0, vbis.getPos());
		assertEquals("C1", intToHex(vbis.read()));
		assertEquals(9, vbis.getPos());
		assertEquals(5, vbis.getBlockLen());
		assertEquals(5, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals(-1, vbis.read());
	}
	
	@Test
	public void testReadOneBlockTwoRecords() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("000E000000050000C100050000C2"));
		RecfmVBInputStream vbis = new RecfmVBInputStream(is);
		assertEquals(0, vbis.getPos());
		assertEquals("C1", intToHex(vbis.read()));
		assertEquals(9, vbis.getPos());
		assertEquals(10, vbis.getBlockLen());
		assertEquals(5, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals("C2", intToHex(vbis.read()));
		assertEquals(14, vbis.getPos());
		assertEquals(10, vbis.getBlockLen());
		assertEquals(10, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals(-1, vbis.read());
	}
	
	@Test
	public void testReadTwoBlocksThreeRecords() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("0009000000050000C1000E000000050000C200050000C3"));
		RecfmVBInputStream vbis = new RecfmVBInputStream(is);
		assertEquals(0, vbis.getPos());
		assertEquals("C1", intToHex(vbis.read()));
		assertEquals(9, vbis.getPos());
		assertEquals(5, vbis.getBlockLen());
		assertEquals(5, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals("C2", intToHex(vbis.read()));
		assertEquals(18, vbis.getPos());
		assertEquals(10, vbis.getBlockLen());
		assertEquals(5, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals("C3", intToHex(vbis.read()));
		assertEquals(23, vbis.getPos());
		assertEquals(10, vbis.getBlockLen());
		assertEquals(10, vbis.getBlockPos());
		assertEquals(1, vbis.getRecordLen());
		assertEquals(1, vbis.getRecordPos());
		assertEquals(-1, vbis.read());
	}

	public byte[] hexToBytes(String s) {
		HexFormat hex = HexFormat.of().withUpperCase();
		return hex.parseHex(s);
	}

	public String intToHex(int c) {
		return intToHex(new byte[] { (byte) c });
	}

	public String intToHex(byte[] b) {
		HexFormat hex = HexFormat.of().withUpperCase();
		return hex.formatHex(b);
	}

	@Test
	public void testReadBytes() throws IOException {
		byte[] data = hexToBytes("000F000000060000C1C100050000C2000B000000070000C3C3C3");
		assertEquals("", read(data, new byte[0], 0));
		assertEquals("C1", read(data, new byte[1], 1));
		assertEquals("C1C1", read(data, new byte[2], 2));
		assertEquals("C1C1C2", read(data, new byte[3], 3));
		assertEquals("C1C1C2C3", read(data, new byte[4], 4));
		assertEquals("C1C1C2C3C3", read(data, new byte[5], 5));
		assertEquals("C1C1C2C3C3C3", read(data, new byte[6], 6));
		assertEquals("C1C1C2C3C3C300", read(data, new byte[7], 6));
	}
	
	private String read(byte[] data, byte[] buf, int expectedCount) throws IOException {
		try (ByteArrayInputStream is = new ByteArrayInputStream(data);
				RecfmVBInputStream vbis = new RecfmVBInputStream(is);) {
			assertEquals(expectedCount, vbis.read(buf));
			return intToHex(buf);
		}
	}
	
}
