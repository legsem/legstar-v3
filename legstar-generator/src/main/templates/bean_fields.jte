@import org.legstar.cobol.generator.model.RenderingItem
@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@import org.legstar.cobol.generator.model.RenderingChoice
@import org.legstar.cobol.generator.model.RenderingString
@import org.legstar.cobol.generator.model.RenderingFloat
@import org.legstar.cobol.generator.model.RenderingDouble
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@import org.legstar.cobol.generator.model.RenderingBinaryNumber
@param RenderingModel model
@param java.util.List<RenderingItem> fields
<%-- Field declarations --%>
@for(var field : fields)
@if(field instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_declare(model, renderingZoneDecimal)
@elseif(field instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_declare(model, renderingPackedDecimal)
@elseif(field instanceof RenderingBinaryNumber renderingBinaryNumber)
@template.bean_field_binarynumber_declare(model, renderingBinaryNumber)
@elseif(field instanceof RenderingString renderingString)
@template.bean_field_string_declare(model, renderingString)
@elseif(field instanceof RenderingFloat renderingFloat)
@template.bean_field_float_declare(model, renderingFloat)
@elseif(field instanceof RenderingDouble renderingDouble)
@template.bean_field_double_declare(model, renderingDouble)
@elseif(field instanceof RenderingGroup renderingSubGroup)
@template.bean_field_group_declare(model, renderingSubGroup)
@elseif(field instanceof RenderingChoice renderingChoice)
@template.bean_field_choice_declare(model, renderingChoice)
@endif
@endfor
<%-- Getters/Setters --%>
@for(var field : fields)
@if(field instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_getsetters(model, renderingZoneDecimal)
@elseif(field instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_getsetters(model, renderingPackedDecimal)
@elseif(field instanceof RenderingBinaryNumber renderingBinaryNumber)
@template.bean_field_binarynumber_getsetters(model, renderingBinaryNumber)
@elseif(field instanceof RenderingString renderingString)
@template.bean_field_string_getsetters(model, renderingString)
@elseif(field instanceof RenderingFloat renderingFloat)
@template.bean_field_float_getsetters(model, renderingFloat)
@elseif(field instanceof RenderingDouble renderingDouble)
@template.bean_field_double_getsetters(model, renderingDouble)
@elseif(field instanceof RenderingGroup renderingSubGroup)
@template.bean_field_group_getsetters(model, renderingSubGroup)
@elseif(field instanceof RenderingChoice renderingChoice)
@template.bean_field_choice_getsetters(model, renderingChoice)
@endif
@endfor
<%-- Sub classes --%>
@for(var field : fields)
@if(field instanceof RenderingGroup renderingSubGroup)
@template.bean_group(model, renderingSubGroup, true)
@elseif(field instanceof RenderingChoice renderingChoice)
@template.bean_choice(model, renderingChoice)
@endif
@endfor
<%-- To String --%>
@template.bean_fields_tostring(model, fields)