@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@param RenderingModel model
@param RenderingZonedDecimal field

@if(field.isArray())
@template.bean_field_array_declare(field.array())
@endif
    @CobolZonedDecimal(cobolName = "${field.cobolName()}", totalDigits = ${field.totalDigits()}@if(field.fractionDigits() > 0), fractionDigits = ${field.fractionDigits()}@endif@if(field.signLeading()), signLeading = true@endif@if(field.signSeparate()), signSeparate = true@endif@if(field.blankWhenZero()), blankWhenZero = true@endif@if(field.odoObject()), odoObject = true@endif)
    private BigDecimal@if(field.isArray())[]@endif ${field.fieldName()};