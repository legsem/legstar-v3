package org.legstar.cobol.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;

/**
 * Generic generator parameters.
 * 
 * @param <T> a specific generator class
 */
public abstract class CobolGeneratorConfigBase<T extends CobolGeneratorConfigBase<T>> implements CobolGeneratorConfig {

	/**
	 * Generated java classes will use this prefix to form a java package name.
	 */
	private String packageNamePrefix;

	/**
	 * Add a toString on each class and nested class generated
	 */
	private boolean withToString;

	/**
	 * Configuration parameters for the copybook parser.
	 */
	private final CopybookParserConfig parserConfig;

	/**
	 * Create a mutable set of parameters.
	 * 
	 * @param parserConfig configuration parameters for the copybook parser
	 */
	public CobolGeneratorConfigBase(CopybookParserConfig parserConfig) {
		super();
		this.parserConfig = parserConfig;
	}

	/**
	 * Get the package name prefix.
	 * 
	 * @return the package name prefix
	 */
	@Override
	public String packageNamePrefix() {
		return packageNamePrefix;
	}

	/**
	 * Set the package name prefix
	 * 
	 * @param packageNamePrefix the package name prefix
	 * @return actual instance
	 */
	public T setPackageNamePrefix(String packageNamePrefix) {
		this.packageNamePrefix = packageNamePrefix;
		return self();
	}

	/**
	 * Should the generated bean include a toString method.
	 * 
	 * @return true if the generated bean include a toString method
	 */
	@Override
	public boolean withToString() {
		return withToString;
	}

	/**
	 * Set whether the generated bean should include a toString method
	 * 
	 * @param withToString should the generated bean include a toString method
	 * @return actual instance
	 */
	public T setWithToString(boolean withToString) {
		this.withToString = withToString;
		return self();
	}

	/**
	 * Generator will parse input copybook using these parameters.
	 * 
	 * @return the copybook parser configuration parameters
	 */
	public CopybookParserConfig parserConfig() {
		return parserConfig;
	}

	/**
	 * Inherited fluent interface.
	 * 
	 * @return actual instance
	 */
	public abstract T self();

}
