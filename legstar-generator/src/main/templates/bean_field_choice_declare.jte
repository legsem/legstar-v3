@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingChoice
@param RenderingModel model
@param RenderingChoice field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    private ${field.className()}@if(field.isArray())[]@endif ${field.fieldName()};