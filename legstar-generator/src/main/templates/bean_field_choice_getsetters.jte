@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingChoice
@param RenderingModel model
@param RenderingChoice field

!{var fieldName = field.fieldName();}
!{var className = field.className();}

    public ${className}@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(${className}@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }