@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param RenderingBinaryNumber field_model

!{var fieldName = model.fieldName(field_model.cobolName());}
!{var typeName = field_model.typeName();}

    public ${typeName}@if(field_model.isArray())[]@endif ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(${typeName}@if(field_model.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }