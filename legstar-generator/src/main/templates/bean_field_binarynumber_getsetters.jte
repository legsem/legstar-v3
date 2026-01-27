@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field

!{var fieldName = field.fieldName();}
!{var typeName = field.typeName();}

    public ${typeName}@if(field.isArray())[]@endif ${field.getterName()}() {
        return ${fieldName};
    }

    public void ${field.setterName()}(${typeName}@if(field.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }