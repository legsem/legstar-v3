package org.legstar.cobol.generator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class CobolBeanGeneratorTest extends CobolGeneratorTestBase {

	private static final Path COPYBOOKS = Paths.get("src/test/copybook");

	private CobolBeanGeneratorConfig config = new CobolBeanGeneratorConfig().setPackageNamePrefix("legstar.samples");

	@Test
	public void testFlat01() {
		check(generate("FLAT01"));
	}

	private String generate(String source) {
		StringWriter sw = new StringWriter();
		new CobolBeanGenerator(config).generate(source, getReader(source), sw);
		return sw.toString();
	}

	private Reader getReader(String source) {
		try {
			Path sourcePath = COPYBOOKS.resolve(source);
			return Files.newBufferedReader(sourcePath, StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
