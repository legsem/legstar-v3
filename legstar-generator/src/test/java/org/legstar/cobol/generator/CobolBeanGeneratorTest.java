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
	public void testAlltypes() {
		check(generate("ALLTYPES"));
	}

	@Test
	public void testFlat01() {
		check(generate("FLAT01"));
	}

	@Test
	public void testFlat02() {
		check(generate("FLAT02"));
	}

	@Test
	public void testStru01() {
		check(generate("STRU01"));
	}

	@Test
	public void testStru03() {
		check(generate("STRU03"));
	}

	@Test
	public void testArdo01() {
		check(generate("ARDO01"));
	}

	@Test
	public void testArdo03() {
		check(generate("ARDO03"));
	}

	@Test
	public void testArdo04() {
		check(generate("ARDO04"));
	}

	@Test
	public void testOptl01() {
		check(generate("OPTL01"));
	}

	@Test
	public void testRdef01() {
		check(generate("RDEF01"));
	}

	@Test
	public void testRdef02() {
		check(generate("RDEF02"));
	}

	@Test
	public void testRdef03() {
		check(generate("RDEF03"));
	}

	@Test
	public void testRdef04() {
		check(generate("RDEF04"));
	}

	@Test
	public void testRdef05() {
		check(generate("RDEF05"));
	}

	@Test
	public void testRdef06() {
		check(generate("RDEF06"));
	}

	@Test
	public void testRdef07() {
		check(generate("RDEF07"));
	}

	@Test
	public void testCustdat() {
		check(generate("CUSTDAT"));
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
