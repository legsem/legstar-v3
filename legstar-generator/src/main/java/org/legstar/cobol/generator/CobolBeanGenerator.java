package org.legstar.cobol.generator;

import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Generates a java bean using a cobol copybook.
 * <p>
 * The generated java bean holds cobol anotations which are recognized by the
 * legstar converter.
 */
public class CobolBeanGenerator extends CobolBeanGeneratorBase {

	public CobolBeanGenerator(CobolBeanGeneratorConfig config) {
		super(config);
	}

	public RenderingOptions renderingOptions() {
		return RenderingOptions.defaults();
	}

}
