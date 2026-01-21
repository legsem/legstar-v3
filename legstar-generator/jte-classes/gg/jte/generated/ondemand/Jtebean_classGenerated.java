package gg.jte.generated.ondemand;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingGroup;
import org.legstar.cobol.generator.model.RenderingString;
import org.legstar.cobol.generator.model.RenderingZonedDecimal;
import org.legstar.cobol.generator.model.RenderingPackedDecimal;
import org.legstar.cobol.generator.model.RenderingItem;
@SuppressWarnings("unchecked")
public final class Jtebean_classGenerated {
	public static final String JTE_NAME = "bean_class.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,6,6,6,6,7,7,7,7,7,7,13,13,14,15,15,16,16,17,18,19,20,21,21,22,23,23,24,25,25,26,28,29,30,31,31,32,33,33,34,35,35,36,38,38,39,39,6,6,6,6};
	public static void render(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, RenderingModel model) {
		if (model.target_package_name() != null) {
			jteOutput.writeContent("package ");
			jteOutput.writeUserContent(model.target_package_name());
			jteOutput.writeContent(";");
		}
		jteOutput.writeContent("\r\n\r\nimport java.math.BigDecimal;\r\n\r\nimport org.legstar.cobol.type.annotations.*;\r\n\r\n");
		if (model.cobol_item() instanceof RenderingGroup) {
			var renderingGroup = (RenderingGroup) model.cobol_item();
			jteOutput.writeContent("@CobolGroup(cobolName = \"");
			jteOutput.writeUserContent(renderingGroup.cobolName());
			jteOutput.writeContent("\")\r\npublic class ");
			jteOutput.writeUserContent(model.className(renderingGroup.cobolName()));
			jteOutput.writeContent(" {\r\n");
			for (var field_model : renderingGroup.fields()) {
				if (field_model instanceof RenderingZonedDecimal renderingZoneDecimal) {
					gg.jte.generated.ondemand.Jtebean_field_zoneddecimal_declareGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingZoneDecimal);
					jteOutput.writeContent("\r\n");
				} else if (field_model instanceof RenderingPackedDecimal renderingPackedDecimal) {
					gg.jte.generated.ondemand.Jtebean_field_packeddecimal_declareGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingPackedDecimal);
					jteOutput.writeContent("\r\n");
				} else if (field_model instanceof RenderingString renderingString) {
					gg.jte.generated.ondemand.Jtebean_field_string_declareGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingString);
					jteOutput.writeContent("\r\n");
				}
			}
			for (var field_model : renderingGroup.fields()) {
				if (field_model instanceof RenderingZonedDecimal renderingZoneDecimal) {
					gg.jte.generated.ondemand.Jtebean_field_zoneddecimal_getsettersGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingZoneDecimal);
					jteOutput.writeContent("\r\n");
				} else if (field_model instanceof RenderingPackedDecimal renderingPackedDecimal) {
					gg.jte.generated.ondemand.Jtebean_field_packeddecimal_getsettersGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingPackedDecimal);
					jteOutput.writeContent("\r\n");
				} else if (field_model instanceof RenderingString renderingString) {
					gg.jte.generated.ondemand.Jtebean_field_string_getsettersGenerated.render(jteOutput, jteHtmlInterceptor, model, renderingString);
					jteOutput.writeContent("\r\n");
				}
			}
			jteOutput.writeContent("}\r\n");
		}
	}
	public static void renderMap(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		RenderingModel model = (RenderingModel)params.get("model");
		render(jteOutput, jteHtmlInterceptor, model);
	}
}
