@param String package_name
@param String xml_namespace
@XmlSchema( 
      namespace = "${xml_namespace}", 
      elementFormDefault = XmlNsForm.QUALIFIED)

@if(package_name != null)package ${package_name};@endif

import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;