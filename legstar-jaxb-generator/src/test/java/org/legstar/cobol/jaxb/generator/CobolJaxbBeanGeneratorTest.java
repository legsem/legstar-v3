package org.legstar.cobol.jaxb.generator;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;

public class CobolJaxbBeanGeneratorTest extends CobolTestBase {

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
	public void testFlat01WithXmlns() {
		check(generate("FLAT01", getDefaultConfig() //
				.setRootXmlNamespace("http://org.legstar/flat01") //
				.setPackageNamePrefix("legstar.samples.jaxb.xmlns")));
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

	private String generate(String source, CobolJaxbBeanGeneratorConfig config) {
		StringWriter sw = new StringWriter();
		new CobolJaxbBeanGenerator(config).generate(source, getReader(source), sw);
		return sw.toString();
	}

	private String generate(String source) {
		return generate(source, getDefaultConfig());
	}

	public static CobolJaxbBeanGeneratorConfig getDefaultConfig() {
		return new CobolJaxbBeanGeneratorConfig() //
				.setPackageNamePrefix("legstar.samples.jaxb") //
				.setWithToString(true);
	}

	private CobolJaxbBeanGeneratorConfig getFreeformConfig() {
		return getDefaultConfig().setFreeCodeFormat(true);
	}

}
