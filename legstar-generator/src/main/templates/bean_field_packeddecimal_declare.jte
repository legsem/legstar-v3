@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@param RenderingModel model
@param RenderingPackedDecimal field_model

@if(field_model.isArray())
@template.bean_field_array_declare(field_model.array())
@endif
    @CobolPackedDecimal(cobolName = "${field_model.cobolName()}"@if(field_model.signed()), signed = true@endif, totalDigits = ${field_model.totalDigits()}@if(field_model.fractionDigits() > 0), fractionDigits = ${field_model.fractionDigits()}@endif@if(field_model.odoObject()), odoObject = true@endif)
    private BigDecimal@if(field_model.isArray())[]@endif ${model.fieldName(field_model.cobolName())};