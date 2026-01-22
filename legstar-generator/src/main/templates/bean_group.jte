@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@import org.legstar.cobol.generator.model.RenderingString
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@import org.legstar.cobol.generator.model.RenderingItem
@param RenderingModel model
@param RenderingGroup renderingGroup
@param boolean subGroup = false

@CobolGroup(cobolName = "${renderingGroup.cobolName()}")
public@if(subGroup) static@endif class ${model.className(renderingGroup.cobolName())} {
<%-- Field declarations --%>
@for(var field_model : renderingGroup.fields())
@if(field_model instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_declare(model, renderingZoneDecimal)
@elseif(field_model instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_declare(model, renderingPackedDecimal)
@elseif(field_model instanceof RenderingBinaryNumber renderingBinaryNumber)
@template.bean_field_binarynumber_declare(model, renderingBinaryNumber)
@elseif(field_model instanceof RenderingString renderingString)
@template.bean_field_string_declare(model, renderingString)
@elseif(field_model instanceof RenderingGroup renderingSubGroup)
@template.bean_field_group_declare(model, renderingSubGroup)
@endif
@endfor
<%-- Getters/Setters --%>
@for(var field_model : renderingGroup.fields())
@if(field_model instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_getsetters(model, renderingZoneDecimal)
@elseif(field_model instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_getsetters(model, renderingPackedDecimal)
@elseif(field_model instanceof RenderingBinaryNumber renderingBinaryNumber)
@template.bean_field_binarynumber_getsetters(model, renderingBinaryNumber)
@elseif(field_model instanceof RenderingString renderingString)
@template.bean_field_string_getsetters(model, renderingString)
@elseif(field_model instanceof RenderingGroup renderingSubGroup)
@template.bean_field_group_getsetters(model, renderingSubGroup)
@endif
@endfor
<%-- Sub classes --%>
@for(var field_model : renderingGroup.fields())
@if(field_model instanceof RenderingGroup renderingSubGroup)
@template.bean_group(model, renderingSubGroup, true)
@endif
@endfor
}
