@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingString
@param RenderingModel model
@param RenderingString field_model

!{var fieldName = model.fieldName(field_model.cobolName());}

    public String@if(field_model.isArray())[]@endif ${model.getterName(field_model.cobolName())}() {
        return ${fieldName};
    }

    public void ${model.setterName(field_model.cobolName())}(String@if(field_model.isArray())[]@endif ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }