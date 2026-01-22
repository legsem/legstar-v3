@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field_model

    @CobolBinaryNumber(cobolName = "${field_model.cobolName()}"@if(field_model.signed()), signed = true@endif, totalDigits = ${field_model.totalDigits()}@if(field_model.odoObject()), odoObject = true@endif)
    private ${field_model.typeName()} ${model.fieldName(field_model.cobolName())};