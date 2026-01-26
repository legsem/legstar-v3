@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingChoice
@param RenderingModel model
@param RenderingChoice field_model

@if(field_model.isArray())
@template.bean_field_array_declare(field_model.array())
@endif
    private ${model.className(field_model.cobolName())}@if(field_model.isArray())[]@endif ${model.fieldName(field_model.cobolName())};