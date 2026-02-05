@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@param RenderingGroup renderingGroup
@param boolean subGroup = false

@CobolGroup(cobolName = "${renderingGroup.cobolName()}")
@if(!subGroup && model.options().hasMoreRootGroupAnnotations())
${model.options().moreRootGroupAnnotations().apply(renderingGroup)}
@endif
@if(model.options().hasMoreGroupAnnotations())
${model.options().moreGroupAnnotations().apply(renderingGroup)}
@endif
public@if(subGroup) static@endif class ${renderingGroup.className()} {
@template.bean_fields(model, renderingGroup.fields())
}