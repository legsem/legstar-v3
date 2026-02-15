package com.legsem.legstar.base.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class CobolTestBase {

	private static final Path REFERENCES = Paths.get("src/test/ref");
	
	private static final Path COPYBOOKS = Paths.get("src/test/copybook");

	private Path references;
	
	private String testName;

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
	
	public String normalize(String s) {
		return s.replaceAll("\\r\\n?", "\n");
	}

	public Reader getReader(String source) {
		try {
			Path sourcePath = COPYBOOKS.resolve(source);
			return Files.newBufferedReader(sourcePath, StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
