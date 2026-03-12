package org.legstar.cobol.json.generator;

import org.legstar.cobol.bean.generator.CobolBeanGeneratorBase;
import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Generates a java bean from a cobol copybook.
 * <p>
 * The java bean will hold cobol and JSON annotations.
 */
public class CobolJsonGenerator extends CobolBeanGeneratorBase {

	/**
	 * JSON rendering options.
	 */
	private final CobolJsonRenderingOptions renderingOptions;

	/**
	 * Build the generator.
	 * 
	 * @param config generator parameters
	 */
	public CobolJsonGenerator(CobolJsonGeneratorConfig config) {
		super(config);
		renderingOptions = new CobolJsonRenderingOptions(config);
	}

	@Override
	public RenderingOptions renderingOptions() {
		return renderingOptions;
	}

}
