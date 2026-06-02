package org.legstar.cobol.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;

public class CobolOutputStreamTest {

	@Test
	public void testRecfmFB() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos)) {
			cos.write(HexFormat.of().parseHex("C1"));
			cos.endRecord();
			cos.flush();
			assertEquals("c1", HexFormat.of().formatHex(baos.toByteArray()));
			assertEquals(1, cos.getBytesWritten());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testRecfmV() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos, CobolRecordFormat.V)) {
			cos.write(HexFormat.of().parseHex("C1"));
			cos.endRecord();
			cos.flush();
			assertEquals("00050000c1", HexFormat.of().formatHex(baos.toByteArray()));
			assertEquals(1, cos.getBytesWritten());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testRecfmVB() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos, CobolRecordFormat.VB)) {
			cos.write(HexFormat.of().parseHex("C1"));
			cos.endRecord();
			cos.flush();
			assertEquals("7ff8000000050000c1", HexFormat.of().formatHex(baos.toByteArray()));
			assertEquals(1, cos.getBytesWritten());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
