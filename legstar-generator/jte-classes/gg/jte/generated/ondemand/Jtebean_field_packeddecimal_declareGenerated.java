package gg.jte.generated.ondemand;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingPackedDecimal;
@SuppressWarnings("unchecked")
public final class Jtebean_field_packeddecimal_declareGenerated {
	public static final String JTE_NAME = "bean_field_packeddecimal_declare.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,2,3,3,3,3};
	public static void render(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, RenderingModel model, RenderingPackedDecimal field_model) {
		jteOutput.writeContent("\r\n    @CobolPackedDecimal(cobolName = \"");
		jteOutput.writeUserContent(field_model.cobolName());
		jteOutput.writeContent("\"");
		if (field_model.signed()) {
			jteOutput.writeContent(", signed = true");
		}
		jteOutput.writeContent(", totalDigits = ");
		jteOutput.writeUserContent(field_model.totalDigits());
		if (field_model.fractionDigits() > 0) {
			jteOutput.writeContent(", fractionDigits = ");
			jteOutput.writeUserContent(field_model.fractionDigits());
		}
		if (field_model.odoObject()) {
			jteOutput.writeContent(", odoObject = true");
		}
		jteOutput.writeContent(")\r\n    private BigDecimal ");
		jteOutput.writeUserContent(model.fieldName(field_model.cobolName()));
		jteOutput.writeContent(";");
	}
	public static void renderMap(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		RenderingModel model = (RenderingModel)params.get("model");
		RenderingPackedDecimal field_model = (RenderingPackedDecimal)params.get("field_model");
		render(jteOutput, jteHtmlInterceptor, model, field_model);
	}
}
