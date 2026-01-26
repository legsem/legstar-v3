@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@param RenderingGroup renderingGroup
@param boolean subGroup = false

@CobolGroup(cobolName = "${renderingGroup.cobolName()}")
public@if(subGroup) static@endif class ${model.className(renderingGroup.cobolName())} {
@template.bean_fields(model, renderingGroup.fields())
}