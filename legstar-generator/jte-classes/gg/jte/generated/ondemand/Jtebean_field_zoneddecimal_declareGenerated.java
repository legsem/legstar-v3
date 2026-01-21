package gg.jte.generated.ondemand;
import org.legstar.cobol.generator.model.RenderingModel;
import org.legstar.cobol.generator.model.RenderingZonedDecimal;
@SuppressWarnings("unchecked")
public final class Jtebean_field_zoneddecimal_declareGenerated {
	public static final String JTE_NAME = "bean_field_zoneddecimal_declare.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,2,3,3,3,3};
	public static void render(gg.jte.TemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, RenderingModel model, RenderingZonedDecimal field_model) {
		jteOutput.writeContent("\r\n    @CobolZonedDecimal(cobolName = \"");
		jteOutput.writeUserContent(field_model.cobolName());
		jteOutput.writeContent("\", totalDigits = ");
		jteOutput.writeUserContent(field_model.totalDigits());
		if (field_model.fractionDigits() > 0) {
			jteOutput.writeContent(", fractionDigits = ");
			jteOutput.writeUserContent(field_model.fractionDigits());
		}
		if (field_model.signLeading()) {
			jteOutput.writeContent(", signLeading = true");
		}
		if (field_model.signSeparate()) {
			jteOutput.writeContent(", signSeparate = true");
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
		RenderingZonedDecimal field_model = (RenderingZonedDecimal)params.get("field_model");
		render(jteOutput, jteHtmlInterceptor, model, field_model);
	}
}
