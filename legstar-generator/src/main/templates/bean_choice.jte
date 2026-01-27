@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingChoice
@param RenderingModel model
@param RenderingChoice renderingChoice

@CobolChoice(maxBytesLen = ${renderingChoice.maxBytesLen()})
public static class ${renderingChoice.className()} {
@template.bean_fields(model, renderingChoice.alternatives())
}