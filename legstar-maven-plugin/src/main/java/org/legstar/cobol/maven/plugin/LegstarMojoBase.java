package org.legstar.cobol.maven.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
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

	private static final String DEFAULT_TARGET_FOLDER = "target/generated-sources";

	private static final String DEFAULT_SOURCE_FOLDER = "src/main/cobol";

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

	/**
	 * True if the cobol copybooks are in free code format (not restricted to
	 * columns 8-72).
	 */
	@Parameter(property = "freeCodeFormat")
	private boolean freeCodeFormat;

	/**
	 * The maven project
	 */
	@Parameter(required = true, property = "project")
	protected MavenProject project;

	/**
	 * Builds a goal.
	 */
	public LegstarMojoBase() {

	}

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

	/**
	 * Check parameters passed on the goal.
	 * 
	 * @throws MojoFailureException if parameters are invalid
	 */
	private void validate() throws MojoFailureException {
		if (source == null) {
			File defaultSource = new File(DEFAULT_SOURCE_FOLDER);
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
			outputDirectory = new File(DEFAULT_TARGET_FOLDER);
		}
		outputDirectory.mkdirs();

	}

	/**
	 * Given a cobol copybook file, generate an output file.
	 * 
	 * @param cobolFile         the cobol file
	 * @param cobolFileEncoding the cobol file encoding
	 * @param output            the output file
	 */
	public abstract void execute(File cobolFile, String cobolFileEncoding, File output);

	/**
	 * Extract the base name of a cobol file. This is for reporting only.
	 * 
	 * @param cobolFile the cobol file
	 * @return the base name (simple lower case name without an extension)
	 */
	public String toBaseName(File cobolFile) {
		String baseName = cobolFile.getName().toLowerCase();
		if (baseName.contains(".")) {
			baseName = baseName.substring(0, baseName.lastIndexOf("."));
		}
		return baseName;
	}

	/**
	 * Given a cobol copybook file, get a reader.
	 * 
	 * @param cobolFile         the cobol file
	 * @param cobolFileEncoding the cobol file encoding
	 * @return a Reader
	 * @throws IOException if reader cannot be created
	 */
	public Reader getReader(File cobolFile, String cobolFileEncoding) throws IOException {
		Charset charset = cobolFileEncoding == null ? Charset.defaultCharset() : Charset.forName(cobolFileEncoding);
		return new FileReader(cobolFile, charset);
	}

	/**
	 * A folder or file for input cobol copybooks.
	 * 
	 * @return The folder or file for input cobol copybooks
	 */
	public File getSource() {
		return source;
	}

	/**
	 * Folder where output files should be created
	 * 
	 * @return the folder where output files should be created
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Character encoding for the COBOL copybooks
	 * 
	 * @return the character encoding for the COBOL copybooks
	 */
	public String getInputEncoding() {
		return inputEncoding;
	}

	/**
	 * Java package name prefix for generated java classes
	 * 
	 * @return The java package name prefix for generated java classes
	 */
	public String getPackageNamePrefix() {
		return packageNamePrefix;
	}

	/**
	 * Whether generated java beans must implement a toString method
	 * 
	 * @return true if the generated java beans must implement a toString method
	 */
	public boolean isWithToString() {
		return withToString;
	}

	/**
	 * Whether the cobol copybooks are in free code format (not restricted to
	 * columns 8-72)
	 * 
	 * @return true if the cobol copybooks are in free code format
	 */
	public boolean isFreeCodeFormat() {
		return freeCodeFormat;
	}

}
