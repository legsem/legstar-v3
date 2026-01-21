package org.legstar.cobol.generator;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.legstar.cobol.copybook.parser.CopybookParser;
import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingModelGenerator;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.WriterOutput;
import gg.jte.resolve.ResourceCodeResolver;

public class CobolBeanGenerator {

	private final CobolBeanGeneratorConfig config;

	public CobolBeanGenerator(CobolBeanGeneratorConfig config) {
		this.config = config;
	}

	public void generate(String inputSource, Reader reader, Writer writer) {
		CopybookParser parser = new CopybookParser();
		List<CobolDataEntry> entries = parser.parse(inputSource, reader);
		RenderingModel renderingModel = new RenderingModelGenerator().generate(inputSource, entries.get(0),
				config.packageNamePrefix());
		generate(renderingModel, writer);
	}

	public void generate(RenderingModel renderingModel, Writer writer) {
		CodeResolver codeResolver = new ResourceCodeResolver("templates", this.getClass().getClassLoader());
		TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Plain);
		templateEngine.setTrimControlStructures(true);
		TemplateOutput output = new WriterOutput(writer);
		templateEngine.render("bean_class.jte", renderingModel, output);
	}

}
