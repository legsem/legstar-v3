@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@param RenderingModel model
@param RenderingZonedDecimal field

!{var fieldName = field.fieldName();}

    public BigDecimal@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(BigDecimal@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }