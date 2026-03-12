package org.legstar.cobol.json.generator;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.copybook.parser.CopybookParserConfig;

public class CobolJsonGeneratorTest extends CobolTestBase {

	@Test
	public void testALLTYPES() {
		check(generate("ALLTYPES"));
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
	public void testCUSTDAT() {
		check(generate("CUSTDAT"));
	}

	@Test
	public void testDIGITNAME() {
		check(generate("DIGITNAME"));
	}

	@Test
	public void testFlat01() {
		check(generate("FLAT01"));
	}

	@Test
	public void testFlat01NoPropOrder() {
		check(generate("FLAT01", getDefaultConfig().setKeepPropertiesOrder(false)));
	}

	@Test
	public void testFlat02() {
		check(generate("FLAT02"));
	}

	@Test
	public void testFreeform() {
		check(generate("FREEFORM", getFreeformConfig()));
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
	public void testStru01() {
		check(generate("STRU01"));
	}

	@Test
	public void testStru03() {
		check(generate("STRU03"));
	}

	private String generate(String source, CobolJsonGeneratorConfig config) {
		StringWriter sw = new StringWriter();
		new CobolJsonGenerator(config).generate(source, getReader(source), sw);
		return sw.toString();
	}

	private String generate(String source) {
		return generate(source, getDefaultConfig());
	}

	public static CobolJsonGeneratorConfig getDefaultConfig() {
		return new CobolJsonGeneratorConfig(new CopybookParserConfig()) //
				.setPackageNamePrefix("legstar.samples.json") //
				.setWithToString(true);
	}

	private CobolJsonGeneratorConfig getFreeformConfig() {
		CobolJsonGeneratorConfig config = getDefaultConfig();
		config.parserConfig().setFreeCodeFormat(true);
		return config;
	}

}
