package org.legstar.cobol.json.generator;

import org.legstar.cobol.copybook.parser.CopybookParserConfig;
import org.legstar.cobol.generator.CobolGeneratorConfigBase;

/**
 * Configuration parameters for the bean generator with JSON annotations.
 */
public class CobolJsonGeneratorConfig extends CobolGeneratorConfigBase<CobolJsonGeneratorConfig> {

	/**
	 * Keep the JSON fields order identical to original cobol group item.
	 */
	private boolean keepPropertiesOrder = true;

	/**
	 * Create a mutable set of parameters.
	 */
	public CobolJsonGeneratorConfig() {
		this(new CopybookParserConfig());
	}

	/**
	 * Create a mutable set of parameters.
	 * 
	 * @param parserConfig configuration parameters for the copybook parser
	 */
	public CobolJsonGeneratorConfig(CopybookParserConfig parserConfig) {
		super(parserConfig);
	}

	/**
	 * Keep JSON fields order.
	 * 
	 * @return true if properties order should be kept (as in the original copybook)
	 */
	public boolean isKeepPropertiesOrder() {
		return keepPropertiesOrder;
	}

	/**
	 * Set whether properties order should be kept
	 * 
	 * @param keepPropertiesOrder true if properties order should be kept (as in the
	 *                            original copybook)
	 * @return this
	 */
	public CobolJsonGeneratorConfig setKeepPropertiesOrder(boolean keepPropertiesOrder) {
		this.keepPropertiesOrder = keepPropertiesOrder;
		return self();
	}

	@Override
	public CobolJsonGeneratorConfig self() {
		return this;
	}

}
