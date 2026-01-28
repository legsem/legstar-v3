@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingFloat
@param RenderingModel model
@param RenderingFloat field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolFloat(cobolName = "${field.cobolName()}")
    private Float@if(field.isArray())[]@endif ${field.fieldName()};