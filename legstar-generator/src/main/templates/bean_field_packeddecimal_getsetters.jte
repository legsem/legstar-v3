@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@param RenderingModel model
@param RenderingPackedDecimal field

!{var fieldName = field.fieldName();}

    public BigDecimal@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(BigDecimal@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }