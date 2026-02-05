package org.legstar.cobol.jaxb.generator;

import java.util.function.Function;

import org.legstar.cobol.generator.model.RenderingGroup;
import org.legstar.cobol.generator.model.RenderingItem;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Inject JAXB annotations.
 */
public class CobolJaxbRenderingOptions implements RenderingOptions {
	
	private final CobolJaxbBeanGeneratorConfig config;
	
	public CobolJaxbRenderingOptions(CobolJaxbBeanGeneratorConfig config) {
		this.config = config;
	}

	@Override
	public boolean hasMoreImports() {
		return true;
	}

	@Override
	public Function<RenderingModel, String> moreImports() {
		return model -> "import jakarta.xml.bind.annotation.*;";
	}

	@Override
	public boolean hasMoreGroupAnnotations() {
		return config.isKeepPropertiesOrder();
	}

	@Override
	public Function<RenderingGroup, String> moreGroupAnnotations() {
		return group -> {
			StringBuilder sb = new StringBuilder();
			sb.append("@XmlType (propOrder={\"");
			sb.append(String.join("\", \"", group.fields().stream().map(RenderingItem::fieldName).toList()));
			sb.append("\"}");
			return sb.toString();
		};
	}

	@Override
	public boolean hasMoreRootGroupAnnotations() {
		return true;
	}

	@Override
	public Function<RenderingGroup, String> moreRootGroupAnnotations() {
		return group -> {
			StringBuilder sb = new StringBuilder();
			sb.append("@XmlRootElement(name = \"");
			sb.append(group.fieldName());
			sb.append("\", namespace = \"\"}");
			return sb.toString();
		};
	}

}
