package org.legstar.cobol.jaxb.generator;

import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import org.legstar.cobol.generator.CobolBeanGeneratorBase;
import org.legstar.cobol.generator.model.RenderingOptions;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.WriterOutput;

/**
 * Generates a java bean from a cobol copybook.
 * <p>
 * The java bean will hold cobol and JAXB annotations.
 */
public class CobolJaxbBeanGenerator extends CobolBeanGeneratorBase {

	/**
	 * JTE package name for precompiled template classes.
	 */
	private static final String JTE_PACKAGE_NAME_PRECOMPILED = "org.legstar.cobol.jaxb.generator.templates";

	/**
	 * JAXB rendering options.
	 */
	private final CobolJaxbRenderingOptions renderingOptions;

	/**
	 * XML namespace to include as an xmlns attribute on the root element
	 */
	private final String rootXmlNamespace;

	/**
	 * Build the generator.
	 * 
	 * @param config generator parameters
	 */
	public CobolJaxbBeanGenerator(CobolJaxbBeanGeneratorConfig config) {
		super(config);
		renderingOptions = new CobolJaxbRenderingOptions(config);
		rootXmlNamespace = config.getRootXmlNamespace();
	}

	@Override
	public RenderingOptions renderingOptions() {
		return renderingOptions;
	}

	@Override
	public Result generateAndWrite(String inputSource, Reader reader, File outputFolder) {
		Result result = super.generateAndWrite(inputSource, reader, outputFolder);
		if (rootXmlNamespace != null && !rootXmlNamespace.isBlank()) {
			writeJavaClass(result.packageName(), "package-info",
					getPackageInfoContent(result.packageName(), rootXmlNamespace), outputFolder);
		}
		return result;
	}

	/**
	 * A package-info is needed when a namespace must be specified. This is because
	 * the @XmlSchema JAXB annotation is a package-level annotation.
	 * 
	 * @param packageName  the package name
	 * @param xmlNamespace the XML namespace
	 * @return the content of package-info
	 */
	String getPackageInfoContent(String packageName, String xmlNamespace) {
		StringWriter writer = new StringWriter();
		TemplateEngine templateEngine = TemplateEngine.createPrecompiled(null, ContentType.Plain, null,
				JTE_PACKAGE_NAME_PRECOMPILED);
		templateEngine.render("jaxb_bean_package_info.jte",
				Map.of("package_name", packageName, "xml_namespace", xmlNamespace), new WriterOutput(writer));
		return writer.toString();
	}

}
