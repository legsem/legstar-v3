@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@param RenderingModel model
@param RenderingPackedDecimal field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolPackedDecimal(cobolName = "${field.cobolName()}"@if(field.signed()), signed = true@endif, totalDigits = ${field.totalDigits()}@if(field.fractionDigits() > 0), fractionDigits = ${field.fractionDigits()}@endif@if(field.odoObject()), odoObject = true@endif)
    private BigDecimal@if(field.isArray())[]@endif ${field.fieldName()};