@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@param RenderingGroup field_model

!{var fieldName = model.fieldName(field_model.cobolName());}
!{var className = model.className(field_model.cobolName());}

    public ${className} ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(${className} ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }