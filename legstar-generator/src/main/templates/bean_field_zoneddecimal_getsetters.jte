@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@param RenderingModel model
@param RenderingZonedDecimal field_model

!{var fieldName = model.fieldName(field_model.cobolName());}

    public BigDecimal@if(field_model.isArray())[]@endif ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(BigDecimal@if(field_model.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }