@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingDouble
@param RenderingModel model
@param RenderingDouble field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolDouble(cobolName = "${field.cobolName()}")
    private Double@if(field.isArray())[]@endif ${field.fieldName()};