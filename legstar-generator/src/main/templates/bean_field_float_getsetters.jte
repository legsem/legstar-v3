@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingFloat
@param RenderingModel model
@param RenderingFloat field

!{var fieldName = field.fieldName();}

    public Float@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(Float@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }