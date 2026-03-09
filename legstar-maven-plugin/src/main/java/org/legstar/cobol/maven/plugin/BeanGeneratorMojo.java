package org.legstar.cobol.maven.plugin;

import java.io.File;
import java.io.Reader;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.legstar.cobol.generator.CobolBeanGenerator;
import org.legstar.cobol.generator.CobolBeanGeneratorConfig;

/**
 * Goal generates a java bean source code with cobol annotations from a COBOL copybook.
 * 
 */
@Mojo(name = "generate-bean", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class BeanGeneratorMojo extends LegstarMojoBase {

	/**
	 * Create generate-bean goal.
	 */
	public BeanGeneratorMojo() {
		super();
	}

	@Override
	public void execute(File cobolFile, String cobolFileEncoding, File output) {
		getLog().info("Processing COBOL file " + cobolFile);

		try (Reader reader = getReader(cobolFile, cobolFileEncoding)) {

			String baseName = toBaseName(cobolFile);
			CobolBeanGeneratorConfig config = new CobolBeanGeneratorConfig() //
					.setPackageNamePrefix(getPackageNamePrefix()) //
					.setWithToString(isWithToString()) //
					.setFreeCodeFormat(isFreeCodeFormat());
			CobolBeanGenerator gen = new CobolBeanGenerator(config);
			gen.generateAndWrite(baseName, reader, output);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
