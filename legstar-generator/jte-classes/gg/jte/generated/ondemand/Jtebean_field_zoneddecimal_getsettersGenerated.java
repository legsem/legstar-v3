package gg.jte.generated.ondemand;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingZonedDecimal;
@SuppressWarnings("unchecked")
public final class Jtebean_field_zoneddecimal_getsettersGenerated {
	public static final String JTE_NAME = "bean_field_zoneddecimal_getsetters.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,5,5,5,7,7,8,8,11,11,11,11,12,12,12,12,13,13,13,2,3,3,3,3};
	public static void render(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, RenderingModel model, RenderingZonedDecimal field_model) {
		jteOutput.writeContent("\r\n");
		var fieldName = model.fieldName(field_model.cobolName());
		jteOutput.writeContent("    public BigDecimal ");
		jteOutput.writeUserContent(model.getterName(field_model.cobolName()));
		jteOutput.writeContent("() {\r\n        return ");
		jteOutput.writeUserContent(fieldName);
		jteOutput.writeContent(";\r\n    }\r\n\r\n    public void ");
		jteOutput.writeUserContent(model.setterName(field_model.cobolName()));
		jteOutput.writeContent("(BigDecimal ");
		jteOutput.writeUserContent(fieldName);
		jteOutput.writeContent(") {\r\n        this.");
		jteOutput.writeUserContent(fieldName);
		jteOutput.writeContent(" = ");
		jteOutput.writeUserContent(fieldName);
		jteOutput.writeContent(";\r\n    }");
	}
	public static void renderMap(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		RenderingModel model = (RenderingModel)params.get("model");
		RenderingZonedDecimal field_model = (RenderingZonedDecimal)params.get("field_model");
		render(jteOutput, jteHtmlInterceptor, model, field_model);
	}
}
