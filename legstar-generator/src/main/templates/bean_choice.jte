@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingChoice
@param RenderingModel model
@param RenderingChoice renderingChoice

@CobolChoice(cobolName = "${renderingChoice.cobolName()}", maxBytesLen = ${renderingChoice.maxBytesLen()})
@if(model.options().hasMoreChoiceAnnotations())
${model.options().moreChoiceAnnotations().apply(renderingChoice)}
@endif
public static class ${renderingChoice.className()} {
@template.bean_fields(model, renderingChoice.alternatives())
}