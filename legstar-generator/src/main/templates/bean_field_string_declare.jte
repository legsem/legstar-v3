@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolString(cobolName = "${field.cobolName()}", charNum = ${field.charNum()})
    private String@if(field.isArray())[]@endif ${field.fieldName()};