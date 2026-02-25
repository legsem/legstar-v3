package org.legstar.cobol.generator;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

import org.legstar.cobol.copybook.parser.CopybookParser;
import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingModelGenerator;
import org.legstar.cobol.generator.model.RenderingOptions;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;

/**
 * Generates a java bean using a cobol copybook.
 * <p>
 * The cobol copybook is first parsed into a set of CobolDataEntry objects (an
 * Abstract Syntax Tree).
 * <p>
 * The first CobolDataEntry which is a group is used to produce the java bean
 * (other data entries are ignored).
 * <p>
 * The chosen group CobolDataEntry is transformed into a Rendering model.
 * <p>
 * The rendering model serves as input for a set of templates to produce the
 * final java bean.
 */
public abstract class CobolBeanGeneratorBase {

	private final CobolBeanGeneratorConfigBase<?> config;

	private final CopybookParser parser;

	/**
	 * Build the generator.
	 * 
	 * @param config generator parameters
	 */
	public CobolBeanGeneratorBase(CobolBeanGeneratorConfigBase<?> config) {
		this.config = config;
		parser = new CopybookParser(config);
	}

	/**
	 * Generate a java bean class using a cobol copybook source.
	 * 
	 * @param inputSource an identifier for the source
	 * @param reader      reads the cobol copybook
	 * @param writer      writes the java bean class
	 * @return the class and package name used for the generated java bean class
	 */
	public Result generate(String inputSource, Reader reader, Writer writer) {
		CobolDataEntry groupEntry = parse(inputSource, reader);
		RenderingModel renderingModel = new RenderingModelGenerator() //
				.generate(inputSource, groupEntry, config.packageNamePrefix(), config.withToString(),
						renderingOptions());
		generate(renderingModel, writer);
		return new Result(renderingModel.package_name(), renderingModel.root_item().className());
	}

	/**
	 * Parse a cobol copybook, returning the first root group item.
	 */
	private CobolDataEntry parse(String inputSource, Reader reader) {
		List<CobolDataEntry> entries = parser.parse(inputSource, reader);
		Optional<CobolDataEntry> groupEntry = entries.stream() //
				.filter(CobolDataEntry::isGroup) //
				.findFirst();
		if (groupEntry.isEmpty()) {
			throw new CobolBeanGeneratorException("Cobol copybook parser did not return any group data items");
		} else {
			return groupEntry.get();
		}
	}

	/**
	 * This is for derived generators giving them opportunity to inject more code
	 * into the generated java bean.
	 * 
	 * @return optional rendering capabilities
	 */
	public abstract RenderingOptions renderingOptions();

	/**
	 * Apply templates to a set of parameters producing a java bean class.
	 * 
	 * @param renderingModel the rendering model for templates
	 * @param writer         the writer to produce output
	 */
	private void generate(RenderingModel renderingModel, Writer writer) {
		TemplateEngine templateEngine = TemplateEngine.createPrecompiled(ContentType.Plain);
		TemplateOutput output = new BeanIndentWriterOutput(writer);
		templateEngine.render("bean_class.jte", renderingModel, output);
	}

	/**
	 * Generation result.
	 * 
	 * @param packageName the java package name for the generated java bean class
	 * @param className   the generated java bean class name
	 */
	public static record Result(String packageName, String className) {
	};

}
