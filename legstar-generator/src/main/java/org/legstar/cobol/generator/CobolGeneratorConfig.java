package org.legstar.cobol.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;

/**
 * All generators share common configuration parameters.
 */
public interface CobolGeneratorConfig {

	/**
	 * Get the package name prefix.
	 * 
	 * @return the package name prefix
	 */
	String packageNamePrefix();

	/**
	 * Should the generated bean include a toString method.
	 * 
	 * @return true if the generated bean include a toString method
	 */
	boolean withToString();

	/**
	 * Generator will parse input copybook using these parameters.
	 * 
	 * @return the copybook parser configuration parameters
	 */
	CopybookParserConfig parserConfig();

}
