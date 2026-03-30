package org.legstar.cobol.bean.generator;

import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Generates a java bean using a cobol copybook.
 * <p>
 * The generated java bean holds cobol anotations which are recognized by the
 * legstar converter.
 */
public class CobolBeanGenerator extends CobolBeanGeneratorBase {

	/**
	 * Build a java bean generator with default configuration parameters.
	 */
	public CobolBeanGenerator() {
		this(new CobolBeanGeneratorConfig());
	}

	/**
	 * Build a java bean generator.
	 * 
	 * @param config generation parameters
	 */
	public CobolBeanGenerator(CobolBeanGeneratorConfig config) {
		super(config);
	}

	@Override
	public RenderingOptions renderingOptions() {
		return RenderingOptions.defaults();
	}

}
