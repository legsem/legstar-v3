@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field_model

@if(field_model.isArray())
@template.bean_field_array_declare(field_model.array())
@endif
    @CobolBinaryNumber(cobolName = "${field_model.cobolName()}"@if(field_model.signed()), signed = true@endif, totalDigits = ${field_model.totalDigits()}@if(field_model.odoObject()), odoObject = true@endif)
    private ${field_model.typeName()}@if(field_model.isArray())[]@endif ${model.fieldName(field_model.cobolName())};