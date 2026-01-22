@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@param RenderingGroup field_model

    private ${model.className(field_model.cobolName())} ${model.fieldName(field_model.cobolName())};