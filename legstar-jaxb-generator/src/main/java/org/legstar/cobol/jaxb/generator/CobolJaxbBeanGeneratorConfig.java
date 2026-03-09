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
	 * Include an xmlns on the root element (null if no namespace to be included).
	 */
	private String rootXmlNamespace;

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

	/**
	 * xmlns property for the root element
	 * 
	 * @return xmlns property for the root element
	 */
	public String getRootXmlNamespace() {
		return rootXmlNamespace;
	}

	/**
	 * xmlns property for the root element
	 * 
	 * @param rootXmlNamespace xmlns property for the root element
	 * @return this
	 */
	public CobolJaxbBeanGeneratorConfig setRootXmlNamespace(String rootXmlNamespace) {
		this.rootXmlNamespace = rootXmlNamespace;
		return self();
	}

	@Override
	public CobolJaxbBeanGeneratorConfig self() {
		return this;
	}

}
