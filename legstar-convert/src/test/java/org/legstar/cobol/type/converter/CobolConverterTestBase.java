package org.legstar.cobol.type.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class CobolConverterTestBase {

	static final Path REFERENCES = Paths.get("src/test/ref");
	
	Path references;
	
	String testName;

	CobolConverterConfig config = CobolConverterConfig.ebcdic();
	
	@BeforeEach
	void setUp(TestInfo testInfo) {
		String s = testInfo.getDisplayName();
		String testClass = testInfo.getTestClass().get().getSimpleName();
		testName = s.substring(0, s.indexOf("("));
		references = REFERENCES.resolve(testClass);
		references.toFile().mkdirs();
	}

	public byte[] hexToBytes(String s) {
		HexFormat hex = HexFormat.of().withUpperCase();
		return hex.parseHex(s);
	}
	
	public InputStream inputStreamFrom(String hex) {
		return new ByteArrayInputStream(hexToBytes(hex));
	}
 
	public void check(String res) {
		try {
			Path refPath = references.resolve(testName);
			if (refPath.toFile().exists()) {
				String ref = Files.readString(refPath);
				assertEquals(normalize(ref), normalize(res));
			} else {
				Files.writeString(refPath, normalize(res));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTestName() {
		return testName;
	}
	
	private String normalize(String s) {
		return s.replaceAll("\\r\\n?", "\n");
	}
}
