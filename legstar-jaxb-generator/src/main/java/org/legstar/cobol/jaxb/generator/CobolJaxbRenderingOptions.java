package org.legstar.cobol.jaxb.generator;

import java.util.function.Function;

import org.legstar.cobol.generator.model.RenderingGroup;
import org.legstar.cobol.generator.model.RenderingItem;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Inject JAXB annotations as additional rendering options.
 */
public class CobolJaxbRenderingOptions implements RenderingOptions {
	
	private static final String NL = System.getProperty("line.separator");

	private final CobolJaxbBeanGeneratorConfig config;
	
	/**
	 * Create the additional JAXD rendering options. 
	 * @param config configuration parameters
	 */
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
		return config.isKeepPropertiesOrder() || config.isXmlAccessTypeField();
	}

	@Override
	public Function<RenderingGroup, String> moreGroupAnnotations() {
		return group -> {
			StringBuilder sb = new StringBuilder();
			if (config.isXmlAccessTypeField()) {
				sb.append("@XmlAccessorType(XmlAccessType.FIELD)");
				if (config.isKeepPropertiesOrder()) {
					sb.append(NL);
				}
			}
			if (config.isKeepPropertiesOrder()) {
				sb.append("@XmlType (propOrder={\"");
				sb.append(String.join("\", \"", group.fields().stream().map(RenderingItem::fieldName).toList()));
				sb.append("\"})");
			}
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
			sb.append("\", namespace = \"\")");
			return sb.toString();
		};
	}

}
