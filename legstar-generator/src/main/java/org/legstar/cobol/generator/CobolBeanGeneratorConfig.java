package org.legstar.cobol.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;

public class CobolBeanGeneratorConfig extends CopybookParserConfig {
	
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

	@Override
	public CobolBeanGeneratorConfig setFreeCodeFormat(boolean freeCodeFormat) {
		return (CobolBeanGeneratorConfig) super.setFreeCodeFormat(freeCodeFormat);
	}

	@Override
	public CobolBeanGeneratorConfig setStartColumn(int startColumn) {
		return (CobolBeanGeneratorConfig) super.setStartColumn(startColumn);
	}

	@Override
	public CobolBeanGeneratorConfig setEndColumn(int endColumn) {
		return (CobolBeanGeneratorConfig) super.setEndColumn(endColumn);
	}

}
