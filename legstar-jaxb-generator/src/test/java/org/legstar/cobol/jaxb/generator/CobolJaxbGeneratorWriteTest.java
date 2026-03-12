package org.legstar.cobol.jaxb.generator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;

public class CobolJaxbGeneratorWriteTest extends CobolTestBase {
	
	@Test
	public void generateAndWriteCustdat() throws IOException {
		File outputDirectory = generateAndWrite("CUSTDAT", getDefaultConfig());
		assertTrue(new File(outputDirectory, "legstar/samples/jaxb/custdat/CustomerData.java").exists());
	}
	
	@Test
	public void generateAndWriteFlat01Xmlns() throws IOException {
		File outputDirectory = generateAndWrite("FLAT01", getDefaultConfig() //
				.setRootXmlNamespace("http://org.legstar/flat01") //
				.setPackageNamePrefix("legstar.samples.jaxb.xmlns"));
		assertTrue(new File(outputDirectory, "legstar/samples/jaxb/xmlns/flat01/Flat01Record.java").exists());
		assertTrue(new File(outputDirectory, "legstar/samples/jaxb/xmlns/flat01/package-info.java").exists());
	}
	
	private File generateAndWrite(String source, CobolJaxbGeneratorConfig config) throws IOException {
		File outputDirectory = Files.createTempDirectory(getTestName()).toFile();
		CobolJaxbGenerator gen = new CobolJaxbGenerator(config);
		gen.generateAndWrite(source, getReader(source), outputDirectory);
		return outputDirectory;
	}
	
	private CobolJaxbGeneratorConfig getDefaultConfig() {
		return CobolJaxbGeneratorTest.getDefaultConfig();
	}

}
