@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field

!{var fieldName = field.fieldName();}

    public String@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(String@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }