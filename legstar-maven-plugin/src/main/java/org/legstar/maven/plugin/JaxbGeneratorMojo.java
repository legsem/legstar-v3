package org.legstar.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.legstar.cobol.jaxb.generator.CobolJaxbBeanGenerator;
import org.legstar.cobol.jaxb.generator.CobolJaxbBeanGeneratorConfig;

/**
 * Goal generates a java bean source code with cobol and jaxb annotations from a
 * COBOL copybook.
 */
@Mojo(name = "generate-jaxb", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JaxbGeneratorMojo extends LegstarMojoBase {

	public void execute(File cobolFile, String cobolFileEncoding, File output) {
		getLog().info("Processing COBOL file " + cobolFile);

		try (Reader reader = getReader(cobolFile, cobolFileEncoding)) {

			CobolJaxbBeanGeneratorConfig config = new CobolJaxbBeanGeneratorConfig() //
					.setPackageNamePrefix(getPackageNamePrefix()) //
					.setWithToString(isWithToString()) //
					.setFreeCodeFormat(isFreeCodeFormat());
			CobolJaxbBeanGenerator gen = new CobolJaxbBeanGenerator(config);

			String baseName = toBaseName(cobolFile);
			StringWriter writer = new StringWriter();
			CobolJaxbBeanGenerator.Result result = gen.generate(baseName, reader, writer);

			writeJavaClass(result.packageName(), result.className(), writer.toString(), output);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
