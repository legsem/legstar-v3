@import gg.jte.support.ForSupport
@import org.legstar.cobol.generator.model.RenderingItem
@import org.legstar.cobol.generator.model.RenderingModel
@param RenderingModel model
@param java.util.List<RenderingItem> fields

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
@for(var field : ForSupport.of(fields))
!{var fieldName = field.get().fieldName();}
            .append("@if(field.getIndex() > 0), @endif${fieldName}=").append(@if(field.get().isArray())Arrays.toString(${fieldName})@else${fieldName}@endif)
@endfor
            .append("}");
        return sb.toString();
    }