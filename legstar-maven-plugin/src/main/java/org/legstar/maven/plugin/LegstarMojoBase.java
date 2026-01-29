package org.legstar.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Properties that are common to all Mojos.
 * 
 */
public abstract class LegstarMojoBase extends AbstractMojo {

	/**
	 * The source can either be a single COBOL copybook file or a directory
	 * containing COBOL copybooks.
	 * <p>
	 * In the case where source is a directory, all files within this directory and
	 * its sub directories will be considered COBOL copybooks.
	 * <p>
	 * Defaults to <b>src/main/cobol</b>.
	 */
	@Parameter(property = "source")
	private File source;

	/**
	 * Output directory where artifacts will be placed. If the directory does not
	 * exist, it will be created.
	 * <p>
	 * Defaults to * <b>target/generated-sources</b>.
	 */
	@Parameter(property = "outputDirectory")
	private File outputDirectory;

	/**
	 * The character encoding for the COBOL copybooks. If this is not specified, the
	 * default platform encoding is assumed.
	 */
	@Parameter(property = "inputEncoding")
	private String inputEncoding;

	@Parameter(required = true, property = "project")
	protected MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {

		validate();

		try {
			if (source.isDirectory()) {
				Files.walk(source.toPath()).filter(Files::isRegularFile)
						.forEach(cpy -> execute(cpy.toFile(), inputEncoding, outputDirectory));
			} else {
				execute(source, inputEncoding, outputDirectory);
			}

			project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
		} catch (IOException e) {
			throw new MojoFailureException(e);
		}
	}

	private void validate() throws MojoFailureException {
		if (source == null) {
			File defaultSource = new File("src/main/cobol");
			if (!defaultSource.exists()) {
				throw new MojoFailureException(
						"Missing a source parameter. This should point to a COBOL copybook file or a directory containing COBOL copybook files.");
			} else {
				source = defaultSource;
			}
		} else if (!source.exists()) {
			throw new MojoFailureException("Provided source'" + source + "' does not exist");
		}

		if (outputDirectory == null) {
			outputDirectory = new File("target/generated-sources/" + getDefaultOutputSubDirectory());
		}
		outputDirectory.mkdirs();

	}

	public abstract void execute(File cobolFile, String cobolFileEncoding, File output);

	public abstract String getDefaultOutputSubDirectory();

	public File getSource() {
		return source;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public String getInputEncoding() {
		return inputEncoding;
	}

}
