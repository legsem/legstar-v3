@import org.legstar.cobol.generator.model.RenderingModel
@import org.legstar.cobol.generator.model.RenderingGroup
@param RenderingModel model
@if(model.target_package_name() != null)package ${model.target_package_name()};@endif

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;
@if(model.cobol_item() instanceof RenderingGroup renderingGroup)
@template.bean_group(model, renderingGroup)
@endif