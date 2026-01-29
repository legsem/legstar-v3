package org.legstar.cobol.generator;

public class CobolBeanGeneratorConfig {

	/**
	 * Generated java classes will use this prefix to form a java package name.
	 */
	private String packageNamePrefix;
	
	/**
	 * Add a toString on each class and nested class generated
	 */
	private boolean withToString;

	public String packageNamePrefix() {
		return packageNamePrefix;
	}

	public CobolBeanGeneratorConfig setPackageNamePrefix(String packageNamePrefix) {
		this.packageNamePrefix = packageNamePrefix;
		return this;
	}

	public boolean withToString() {
		return withToString;
	}

	public CobolBeanGeneratorConfig setWithToString(boolean withToString) {
		this.withToString = withToString;
		return this;
	}

}
