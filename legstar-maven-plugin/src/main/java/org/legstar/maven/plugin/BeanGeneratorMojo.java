package org.legstar.maven.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.legstar.cobol.generator.CobolBeanGenerator;
import org.legstar.cobol.generator.CobolBeanGeneratorConfig;

/**
 * Goal generates java beans from a COBOL copybook.
 * 
 */
@Mojo(name = "generate-bean", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class BeanGeneratorMojo extends LegstarMojoBase {

	/**
	 * The java package name prefix for generated java classes. The copybook name in
	 * lower case forms the last portion of the final package name.
	 */
	@Parameter(property = "packageNamePrefix")
	private String packageNamePrefix;

	/**
	 * True if the generated java beans must implement a toString method for each
	 * class and nested class.
	 */
	@Parameter(property = "withToString")
	private boolean withToString;

	@Override
	public void execute(File cobolFile, String cobolFileEncoding, File output) {
		try {
			getLog().info("Processing COBOL file " + cobolFile);
			CobolBeanGeneratorConfig config = new CobolBeanGeneratorConfig() //
					.setPackageNamePrefix(packageNamePrefix) //
					.setWithToString(withToString);
			CobolBeanGenerator gen = new CobolBeanGenerator(config);
			String baseName = cobolFile.getName().toLowerCase();
			if (baseName.contains(".")) {
				baseName = baseName.substring(0, baseName.lastIndexOf("."));
			}
			Charset charset = cobolFileEncoding == null ? Charset.defaultCharset() : Charset.forName(cobolFileEncoding);
			FileReader reader = new FileReader(cobolFile, charset);
			StringWriter writer = new StringWriter();
			String className = gen.generate(baseName, reader, writer);
			String packageName = packageNamePrefix == null ? baseName
			        : (packageNamePrefix + "." + baseName);

			Path classPath = output.toPath().resolve(packageName.replace(".", "/"));
			classPath.toFile().mkdirs();
			Files.writeString(classPath.resolve(className + ".java"), writer.toString(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPackageNamePrefix() {
		return packageNamePrefix;
	}

	public boolean isWithToString() {
		return withToString;
	}

	public String getDefaultOutputSubDirectory() {
		return "java";
	}

}
