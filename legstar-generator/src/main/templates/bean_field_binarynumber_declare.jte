@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolBinaryNumber(cobolName = "${field.cobolName()}"@if(field.signed()), signed = true@endif, totalDigits = ${field.totalDigits()}@if(field.odoObject()), odoObject = true@endif)
    private ${field.typeName()}@if(field.isArray())[]@endif ${field.fieldName()};