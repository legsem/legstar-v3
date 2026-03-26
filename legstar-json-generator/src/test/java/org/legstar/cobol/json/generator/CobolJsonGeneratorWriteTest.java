package org.legstar.cobol.json.generator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.legstar.cobol.base.test.CobolTestBase;

public class CobolJsonGeneratorWriteTest extends CobolTestBase {
	
	@TempDir
	File outputDirectory;
	
	@Test
	public void generateAndWriteCustdat() throws IOException {
		File outputDirectory = generateAndWrite("CUSTDAT", getDefaultConfig());
		assertTrue(new File(outputDirectory, "legstar/samples/json/custdat/CustomerData.java").exists());
	}
	
	private File generateAndWrite(String source, CobolJsonGeneratorConfig config) throws IOException {
		CobolJsonGenerator gen = new CobolJsonGenerator(config);
		gen.generateAndWrite(source, getReader(source), outputDirectory);
		return outputDirectory;
	}
	
	private CobolJsonGeneratorConfig getDefaultConfig() {
		return CobolJsonGeneratorTest.getDefaultConfig();
	}

}
