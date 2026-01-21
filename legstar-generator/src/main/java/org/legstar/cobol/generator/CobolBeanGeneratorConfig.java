package org.legstar.cobol.generator;

public class CobolBeanGeneratorConfig {

	/**
	 * Generated java classes will use this prefix to form a java package name.
	 */
	private String packageNamePrefix;

	public String packageNamePrefix() {
		return packageNamePrefix;
	}

	public CobolBeanGeneratorConfig setPackageNamePrefix(String packageNamePrefix) {
		this.packageNamePrefix = packageNamePrefix;
		return this;
	}

}
