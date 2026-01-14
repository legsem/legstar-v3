package org.legstar.cobol.copybook.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class TestBase {
	
	private static final Path REFERENCES = Paths.get("src/test/ref");
	
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

	public void check(String res) {
		try {
			Path refPath = references.resolve(testName);
			if (refPath.toFile().exists()) {
				String ref = Files.readString(refPath);
				assertEquals(ref, res);
			} else {
				Files.writeString(refPath, res);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTestName() {
		return testName;
	}

}
