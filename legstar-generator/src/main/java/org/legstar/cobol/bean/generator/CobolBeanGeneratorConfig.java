package org.legstar.cobol.bean.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;
import org.legstar.cobol.generator.CobolGeneratorConfigBase;

/**
 * Java bean generator parameters.
 */
public class CobolBeanGeneratorConfig extends CobolGeneratorConfigBase<CobolBeanGeneratorConfig> {

	/**
	 * Create a mutable set of parameters.
	 */
	public CobolBeanGeneratorConfig() {
		this(new CopybookParserConfig());
	}

	/**
	 * Create a mutable set of parameters.
	 * 
	 * @param parserConfig configuration parameters for the copybook parser
	 */
	public CobolBeanGeneratorConfig(CopybookParserConfig parserConfig) {
		super(parserConfig);
	}

	@Override
	public CobolBeanGeneratorConfig self() {
		return this;
	}

}
