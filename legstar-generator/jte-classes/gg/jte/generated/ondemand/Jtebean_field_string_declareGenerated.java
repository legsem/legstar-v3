package gg.jte.generated.ondemand;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingString;
@SuppressWarnings("unchecked")
public final class Jtebean_field_string_declareGenerated {
	public static final String JTE_NAME = "bean_field_string_declare.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,5,5,5,5,5,6,6,6,6,6,2,3,3,3,3};
	public static void render(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, RenderingModel model, RenderingString field_model) {
		jteOutput.writeContent("\r\n    @CobolString(cobolName = \"");
		jteOutput.writeUserContent(field_model.cobolName());
		jteOutput.writeContent("\", charNum = ");
		jteOutput.writeUserContent(field_model.charNum());
		jteOutput.writeContent(")\r\n    private String ");
		jteOutput.writeUserContent(model.fieldName(field_model.cobolName()));
		jteOutput.writeContent(";");
	}
	public static void renderMap(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		RenderingModel model = (RenderingModel)params.get("model");
		RenderingString field_model = (RenderingString)params.get("field_model");
		render(jteOutput, jteHtmlInterceptor, model, field_model);
	}
}
