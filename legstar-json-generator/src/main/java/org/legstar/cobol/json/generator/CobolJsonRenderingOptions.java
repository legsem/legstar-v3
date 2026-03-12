package org.legstar.cobol.json.generator;

import java.util.function.Function;

import org.legstar.cobol.generator.model.RenderingChoice;
import org.legstar.cobol.generator.model.RenderingGroup;
import org.legstar.cobol.generator.model.RenderingItem;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingOptions;

/**
 * Inject JSON annotations as additional rendering options.
 */
public class CobolJsonRenderingOptions implements RenderingOptions {

	private final CobolJsonGeneratorConfig config;

	/**
	 * Create the additional JSON rendering options.
	 * 
	 * @param config configuration parameters
	 */
	public CobolJsonRenderingOptions(CobolJsonGeneratorConfig config) {
		this.config = config;
	}

	@Override
	public boolean hasMoreImports() {
		return true;
	}

	@Override
	public Function<RenderingModel, String> moreImports() {
		return model -> "import com.fasterxml.jackson.annotation.*;";
	}

	@Override
	public boolean hasMoreGroupAnnotations() {
		return config.isKeepPropertiesOrder();
	}

	@Override
	public Function<RenderingGroup, String> moreGroupAnnotations() {
		return group -> {
			StringBuilder sb = new StringBuilder();
			if (config.isKeepPropertiesOrder()) {
				sb.append("@JsonPropertyOrder ({\"");
				sb.append(String.join("\", \"", group.fields().stream().map(RenderingItem::fieldName).toList()));
				sb.append("\"})");
			}
			return sb.toString();
		};
	}

	@Override
	public boolean hasMoreChoiceAnnotations() {
		return false;
	}

	@Override
	public Function<RenderingChoice, String> moreChoiceAnnotations() {
		return null;
	}

	@Override
	public boolean hasMoreRootGroupAnnotations() {
		return false;
	}

	@Override
	public Function<RenderingGroup, String> moreRootGroupAnnotations() {
		return null;
	}

}
