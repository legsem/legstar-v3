package org.legstar.cobol.bean.generator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.copybook.parser.CopybookParserConfig;

public class CobolBeanGeneratorWriteTest extends CobolTestBase {
	
	@Test
	public void generateAndWriteTest() throws IOException {
		File outputDirectory = Files.createTempDirectory(getTestName()).toFile();
		CobolBeanGeneratorConfig config = new CobolBeanGeneratorConfig(new CopybookParserConfig());
		CobolBeanGenerator gen = new CobolBeanGenerator(config);
		gen.generateAndWrite("CUSTDAT", getReader("CUSTDAT"), outputDirectory);
		assertTrue(new File(outputDirectory, "custdat/CustomerData.java").exists());
	}
}
