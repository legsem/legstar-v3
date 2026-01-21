@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@import org.legstar.cobol.generator.model.RenderingString
@import org.legstar.cobol.generator.model.RenderingZonedDecimal
@import org.legstar.cobol.generator.model.RenderingPackedDecimal
@import org.legstar.cobol.generator.model.RenderingItem
@param RenderingModel model
@if(model.target_package_name() != null)package ${model.target_package_name()};@endif

import java.math.BigDecimal;

import org.legstar.cobol.type.annotations.*;

@if(model.cobol_item() instanceof RenderingGroup)
!{var renderingGroup = (RenderingGroup) model.cobol_item();}
@CobolGroup(cobolName = "${renderingGroup.cobolName()}")
public class ${model.className(renderingGroup.cobolName())} {
<%-- Field declarations --%>
@for(var field_model : renderingGroup.fields())
@if(field_model instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_declare(model = model, field_model = renderingZoneDecimal)
@elseif(field_model instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_declare(model = model, field_model = renderingPackedDecimal)
@elseif(field_model instanceof RenderingString renderingString)
@template.bean_field_string_declare(model = model, field_model = renderingString)
@endif
@endfor
<%-- Getters/Setters --%>
@for(var field_model : renderingGroup.fields())
@if(field_model instanceof RenderingZonedDecimal renderingZoneDecimal)
@template.bean_field_zoneddecimal_getsetters(model = model, field_model = renderingZoneDecimal)
@elseif(field_model instanceof RenderingPackedDecimal renderingPackedDecimal)
@template.bean_field_packeddecimal_getsetters(model = model, field_model = renderingPackedDecimal)
@elseif(field_model instanceof RenderingString renderingString)
@template.bean_field_string_getsetters(model = model, field_model = renderingString)
@endif
@endfor
}
@endif
