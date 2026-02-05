package org.legstar.cobol.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;

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

	public String packageNamePrefix() {
		return packageNamePrefix;
	}

	public T setPackageNamePrefix(String packageNamePrefix) {
		this.packageNamePrefix = packageNamePrefix;
		return self();
	}

	public boolean withToString() {
		return withToString;
	}

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

	public abstract T self();

}
