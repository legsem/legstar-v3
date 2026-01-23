@import org.legstar.cobol.generator.model.RenderingArray
@param RenderingArray renderingArray
    @CobolArray(minOccurs=${renderingArray.minOccurs()}, maxOccurs=${renderingArray.maxOccurs()}@if(renderingArray.isVariableSize()), dependingOn = "${renderingArray.dependingOn()}"@endif)