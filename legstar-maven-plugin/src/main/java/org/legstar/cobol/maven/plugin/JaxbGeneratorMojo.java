package org.legstar.cobol.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.legstar.cobol.copybook.parser.CopybookParserConfig;
import org.legstar.cobol.jaxb.generator.CobolJaxbGenerator;
import org.legstar.cobol.jaxb.generator.CobolJaxbGeneratorConfig;

/**
 * Goal generates a java bean source code with cobol and jaxb annotations from a
 * COBOL copybook.
 */
@Mojo(name = "generate-jaxb", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JaxbGeneratorMojo extends LegstarMojoBase {

	/**
	 * Include an XML namespace (xmlns attribute) on the root element (null if no
	 * namespace to be included).
	 */
	@Parameter(property = "rootXmlNamespace")
	private String rootXmlNamespace;

	/**
	 * Create generate-jaxb goal.
	 */
	public JaxbGeneratorMojo() {
		super();
	}

	public void execute(File cobolFile, String cobolFileEncoding, File output) {
		getLog().info("Processing COBOL file " + cobolFile);

		try (Reader reader = getReader(cobolFile, cobolFileEncoding)) {

			String baseName = toBaseName(cobolFile);
			CopybookParserConfig parserConfig = new CopybookParserConfig() //
					.setFreeCodeFormat(isFreeCodeFormat());
			CobolJaxbGeneratorConfig config = new CobolJaxbGeneratorConfig(parserConfig) //
					.setPackageNamePrefix(getPackageNamePrefix()) //
					.setWithToString(isWithToString()) //
					.setRootXmlNamespace(getRootXmlNamespace());
			CobolJaxbGenerator gen = new CobolJaxbGenerator(config);
			gen.generateAndWrite(baseName, reader, output);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * XML namespace (xmlns attribute) to include on root element
	 * 
	 * @return the XML namespace (xmlns attribute) to include on root element
	 */
	public String getRootXmlNamespace() {
		return rootXmlNamespace;
	}

}
