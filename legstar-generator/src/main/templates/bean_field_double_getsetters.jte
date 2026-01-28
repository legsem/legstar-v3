@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingDouble
@param RenderingModel model
@param RenderingDouble field

!{var fieldName = field.fieldName();}

    public Double@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(Double@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }