@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field_model

@if(field_model.isArray())
@template.bean_field_array_declare(field_model.array())
@endif
    @CobolString(cobolName = "${field_model.cobolName()}", charNum = ${field_model.charNum()})
    private String@if(field_model.isArray())[]@endif ${model.fieldName(field_model.cobolName())};