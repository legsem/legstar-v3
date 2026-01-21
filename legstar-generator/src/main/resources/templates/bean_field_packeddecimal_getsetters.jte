@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@param RenderingModel model
@param RenderingPackedDecimal field_model

!{var fieldName = model.fieldName(field_model.cobolName());}

    public BigDecimal ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(BigDecimal ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }