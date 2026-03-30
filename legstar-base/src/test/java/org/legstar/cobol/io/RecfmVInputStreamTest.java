package org.legstar.cobol.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class RecfmVInputStreamTest {

	@Test
	public void testReadOneRecord() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("00050000C1"));
		RecfmVInputStream vis = new RecfmVInputStream(is);
		assertEquals(0, vis.getPos());
		assertEquals("C1", intToHex(vis.read()));
		assertEquals(5, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals(-1, vis.read());
	}
	
	@Test
	public void testReadTwoRecords() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("00050000C100050000C2"));
		RecfmVInputStream vis = new RecfmVInputStream(is);
		assertEquals(0, vis.getPos());
		assertEquals("C1", intToHex(vis.read()));
		assertEquals(5, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals("C2", intToHex(vis.read()));
		assertEquals(10, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals(-1, vis.read());
	}
	
	@Test
	public void testReadThreeRecords() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(hexToBytes("00050000C100050000C200050000C3"));
		RecfmVInputStream vis = new RecfmVInputStream(is);
		assertEquals(0, vis.getPos());
		assertEquals("C1", intToHex(vis.read()));
		assertEquals(5, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals("C2", intToHex(vis.read()));
		assertEquals(10, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals("C3", intToHex(vis.read()));
		assertEquals(15, vis.getPos());
		assertEquals(1, vis.getRecordLen());
		assertEquals(1, vis.getRecordPos());
		assertEquals(-1, vis.read());
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
		byte[] data = hexToBytes("00060000C1C100050000C200070000C3C3C3");
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
				RecfmVInputStream vis = new RecfmVInputStream(is);) {
			assertEquals(expectedCount, vis.read(buf));
			return intToHex(buf);
		}
	}
	
}
