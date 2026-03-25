Legstar V3
==========
![Supported JVM Versions](https://img.shields.io/badge/JVM-17--25-green?logo=openjdk)
[![License](https://img.shields.io/github/license/legsem/legstar-v3)](https://www.apache.org/licenses/LICENSE-2.0)

This project provides a toolset for Cobol to Java, Xml and JSON data conversion.

Cobol data, as it originates from mainframes, is binary. Legstar uses a cobol copybook (metadata describing the cobol data) to figure out how to convert that binary data into a format that is usable in the Java ecosystem.

Guides:
* [Convert cobol data to a java instance](docs/HOWTO-COBOL-TO-JAVA.md)
* [Convert cobol data to XML](docs/HOWTO-COBOL-TO-XML.md)
* [Convert cobol data to JSON](docs/HOWTO-COBOL-TO-JSON.md)

---
In the long term, legstar-v3 is meant to replace [legstar-core2](https://github.com/legsem/legstar-core2).

The major differences between legstar-v3 and legstar-core2 are:

* The legstar-v3 [copybook parser](https://legsem.github.io/legstar-v3/maven-docs/legstar-parser/site/legstar-parser/apidocs/org.legstar.cobol.parser/module-summary.html) is based on [CongoCC](https://github.com/congo-cc/congo-parser-generator) and has no runtime dependencies. legstar-core2 uses [Antlr3](https://github.com/antlr/antlr3) and requires the Antlr runtime.
* legstar-v3 does not use XML Schemas. Instead, it provides [java annotations for cobol](https://legsem.github.io/legstar-v3/maven-docs/legstar-base/site/legstar-base/apidocs/org.legstar.cobol.base/org/legstar/cobol/annotation/package-summary.html). These annotations are injected in generated java classes by [legstar-generator](https://legsem.github.io/legstar-v3/maven-docs/legstar-generator/site/legstar-generator/apidocs/org.legstar.cobol.generator/module-summary.html). The annotations are used at runtime by [legstar-converter](https://legsem.github.io/legstar-v3/maven-docs/legstar-converter/site/legstar-converter/apidocs/org.legstar.cobol.converter/module-summary.html).
* For XML support, [legstar-jaxb-generator](https://legsem.github.io/legstar-v3/maven-docs/legstar-jaxb-generator/site/legstar-jaxb-generator/apidocs/org.legstar.cobol.jaxb.generator/module-summary.html) injects JAXB Annotations alongside the cobol annotations in the generated java classes. At runtime [legstar-jaxb-converter](https://legsem.github.io/legstar-v3/maven-docs/legstar-jaxb-converter/site/legstar-jaxb-converter/apidocs/org.legstar.cobol.jaxb.converter/module-summary.html) uses these annotations to produce an XML instance.
* legstar-v3 offers JSON support. For this, [legstar-json-generator](https://legsem.github.io/legstar-v3/maven-docs/legstar-json-generator/site/legstar-json-generator/apidocs/org.legstar.cobol.json.generator/module-summary.html) injects JACKSON annotations alongside the cobol annotations in the generated java classes. At runtime [legstar-json-converter](https://legsem.github.io/legstar-v3/maven-docs/legstar-json-converter/site/legstar-json-converter/apidocs/org.legstar.cobol.json.converter/module-summary.html) uses these annotations to produce a JSON instance.
