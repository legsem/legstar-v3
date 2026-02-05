package org.legstar.cobol.jaxb.generator;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.generator.CobolGeneratorTestBase;

public class CobolJaxbBeanGeneratorTest extends CobolGeneratorTestBase {

	@Test
	public void testALLTYPES() {
		check(generate("ALLTYPES"));
	}

	@Test
	public void testFlat01() {
		check(generate("FLAT01"));
	}

	@Test
	public void testFlat01NoPropOrder() {
		check(generate("FLAT01", getDefaultConfig().setKeepPropertiesOrder(false)));
	}

	private String generate(String source, CobolJaxbBeanGeneratorConfig config) {
		StringWriter sw = new StringWriter();
		new CobolJaxbBeanGenerator(config).generate(source, getReader(source), sw);
		return sw.toString();
	}

	private String generate(String source) {
		return generate(source, getDefaultConfig());
	}

	private CobolJaxbBeanGeneratorConfig getDefaultConfig() {
		return new CobolJaxbBeanGeneratorConfig() //
				.setPackageNamePrefix("legstar.samples") //
				.setWithToString(true);
	}

}
