package org.legstar.cobol.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.legstar.cobol.copybook.parser.CopybookParserConfig;
import org.legstar.cobol.json.generator.CobolJsonGenerator;
import org.legstar.cobol.json.generator.CobolJsonGeneratorConfig;

/**
 * Goal generates a java bean source code with cobol and JSON annotations from a
 * COBOL copybook.
 */
@Mojo(name = "generate-json", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JsonGeneratorMojo extends LegstarMojoBase {

	/**
	 * Create generate-json goal.
	 */
	public JsonGeneratorMojo() {
		super();
	}

	public void execute(File cobolFile, String cobolFileEncoding, File output) {
		getLog().info("Processing COBOL file " + cobolFile);

		try (Reader reader = getReader(cobolFile, cobolFileEncoding)) {

			String baseName = toBaseName(cobolFile);
			CopybookParserConfig parserConfig = new CopybookParserConfig() //
					.setFreeCodeFormat(isFreeCodeFormat());
			CobolJsonGeneratorConfig config = new CobolJsonGeneratorConfig(parserConfig) //
					.setPackageNamePrefix(getPackageNamePrefix()) //
					.setWithToString(isWithToString());
			CobolJsonGenerator gen = new CobolJsonGenerator(config);
			gen.generateAndWrite(baseName, reader, output);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
