package org.legstar.cobol.jaxb.generator;

import org.legstar.cobol.generator.CobolBeanGeneratorBase;
import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Generates a java bean from a cobol copybook.
 * <p>
 * The java bean will hold cobol and JAXB annotations.
 */
public class CobolJaxbBeanGenerator extends CobolBeanGeneratorBase {

	private final CobolJaxbRenderingOptions renderingOptions;

	public CobolJaxbBeanGenerator(CobolJaxbBeanGeneratorConfig config) {
		super(config);
		renderingOptions = new CobolJaxbRenderingOptions(config);
	}

	@Override
	public RenderingOptions renderingOptions() {
		return renderingOptions;
	}

}
