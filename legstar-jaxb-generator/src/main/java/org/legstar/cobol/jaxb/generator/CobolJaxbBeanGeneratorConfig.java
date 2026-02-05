package org.legstar.cobol.jaxb.generator;

import org.legstar.cobol.generator.CobolBeanGeneratorConfigBase;

public class CobolJaxbBeanGeneratorConfig extends CobolBeanGeneratorConfigBase<CobolJaxbBeanGeneratorConfig> {

	/**
	 * Keep the XML element order identical to original cobol group item.
	 */
	private boolean keepPropertiesOrder = true;

	/**
	 * Add the @XmlAccessorType(XmlAccessType.FIELD) annotation
	 */
	private boolean xmlAccessTypeField = true;

	public boolean isKeepPropertiesOrder() {
		return keepPropertiesOrder;
	}

	public CobolJaxbBeanGeneratorConfig setKeepPropertiesOrder(boolean keepPropertiesOrder) {
		this.keepPropertiesOrder = keepPropertiesOrder;
		return self();
	}

	@Override
	public CobolJaxbBeanGeneratorConfig self() {
		return this;
	}

	public boolean isXmlAccessTypeField() {
		return xmlAccessTypeField;
	}

	public CobolJaxbBeanGeneratorConfig setXmlAccessTypeField(boolean xmlAccessTypeField) {
		this.xmlAccessTypeField = xmlAccessTypeField;
		return self();
	}

}
