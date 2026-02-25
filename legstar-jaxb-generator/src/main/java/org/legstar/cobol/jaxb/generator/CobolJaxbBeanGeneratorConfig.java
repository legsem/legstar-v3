package org.legstar.cobol.jaxb.generator;

import org.legstar.cobol.generator.CobolBeanGeneratorConfigBase;

/**
 * JAXB bean generator parameters.
 * 
 */
public class CobolJaxbBeanGeneratorConfig extends CobolBeanGeneratorConfigBase<CobolJaxbBeanGeneratorConfig> {

	/**
	 * Keep the XML element order identical to original cobol group item.
	 */
	private boolean keepPropertiesOrder = true;

	/**
	 * Add the @XmlAccessorType(XmlAccessType.FIELD) annotation
	 */
	private boolean xmlAccessTypeField = false;
	
	/**
	 * Create a mutable set of parameters.
	 */
	public CobolJaxbBeanGeneratorConfig() {
		
	}

	/**
	 * Keep Xml properties order.
	 * 
	 * @return true if properties order should be kept (as in the original copybook)
	 */
	public boolean isKeepPropertiesOrder() {
		return keepPropertiesOrder;
	}

	/**
	 * Set whether properties order should be kept
	 * 
	 * @param keepPropertiesOrder true if properties order should be kept (as in the
	 *                            original copybook)
	 * @return this
	 */
	public CobolJaxbBeanGeneratorConfig setKeepPropertiesOrder(boolean keepPropertiesOrder) {
		this.keepPropertiesOrder = keepPropertiesOrder;
		return self();
	}

	/**
	 * Should JAXB access fields directly instead of using getters and setters.
	 * 
	 * @return true if JAXB accesses fields directly instead of using getters and
	 *         setters
	 */
	public boolean isXmlAccessTypeField() {
		return xmlAccessTypeField;
	}

	/**
	 * Set whether JAXB accesses fields directly instead of using getters and
	 * setters
	 * 
	 * @param xmlAccessTypeField true if JAXB accesses fields directly instead of
	 *                           using getters and setters
	 * @return this
	 */
	public CobolJaxbBeanGeneratorConfig setXmlAccessTypeField(boolean xmlAccessTypeField) {
		this.xmlAccessTypeField = xmlAccessTypeField;
		return self();
	}

	@Override
	public CobolJaxbBeanGeneratorConfig self() {
		return this;
	}

}
