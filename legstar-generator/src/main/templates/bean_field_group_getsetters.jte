@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@param RenderingGroup field_model

!{var fieldName = model.fieldName(field_model.cobolName());}
!{var className = model.className(field_model.cobolName());}

    public ${className}@if(field_model.isArray())[]@endif ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(${className}@if(field_model.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }