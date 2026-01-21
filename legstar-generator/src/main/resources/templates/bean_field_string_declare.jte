@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field_model

    @CobolString(cobolName = "${field_model.cobolName()}", charNum = ${field_model.charNum()})
    private String ${model.fieldName(field_model.cobolName())};