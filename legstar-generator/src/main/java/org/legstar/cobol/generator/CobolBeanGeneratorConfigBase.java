package org.legstar.cobol.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;

/**
 * Java bean generator parameters.
 * 
 * @param <T> a specific generator class
 */
public abstract class CobolBeanGeneratorConfigBase<T extends CobolBeanGeneratorConfigBase<T>>
		extends CopybookParserConfig {

	/**
	 * Generated java classes will use this prefix to form a java package name.
	 */
	private String packageNamePrefix;

	/**
	 * Add a toString on each class and nested class generated
	 */
	private boolean withToString;

	/**
	 * Create a mutable set of parameters.
	 */
	public CobolBeanGeneratorConfigBase() {
		super();
	}

	/**
	 * Get the package name prefix.
	 * 
	 * @return the package name prefix
	 */
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

	@Override
	public T setFreeCodeFormat(boolean freeCodeFormat) {
		super.setFreeCodeFormat(freeCodeFormat);
		return self();
	}

	@Override
	public T setStartColumn(int startColumn) {
		super.setStartColumn(startColumn);
		return self();
	}

	@Override
	public T setEndColumn(int endColumn) {
		super.setEndColumn(endColumn);
		return self();
	}

	/**
	 * Inherited fluent interface.
	 * 
	 * @return actual instance
	 */
	public abstract T self();

}
