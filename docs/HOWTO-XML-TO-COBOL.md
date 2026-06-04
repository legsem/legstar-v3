How to convert XML to cobol data
=================================

Objective:
---------
Starting from the cobol copybook [CUSTDAT.cpy](../samples/CUSTDAT.cpy), convert an XML instance into cobol binary data.

Pre-requisites:
--------------
* JDK 17 or newer
* MAVEN 3.9.11 or newer

This is a 2 steps process:
--------------------------
1. Step 1: Generate a java bean class with cobol and JAXB annotations using cobol copybook CUSTDAT.cpy
2. Step 2: Execute legstar-jaxb-converter to produce cobol binary data

Step 1 - Generate a java bean class with cobol and JAXB annotations using CUSTDAT.cpy
------------------------------------------------------------------------------------
Follow the Step 1 instructions found in [How to convert cobol data to XML](./HOWTO-COBOL-TO-XML.md)


Step 2: Execute legstar-jaxb-converter to produce a cobol binary data
---------------------------------------------------------------------

1. Create a new java class named CustomerDataXmlToCobol in some package. Type the following code:

```java
import javax.xml.transform.stream.StreamSource;

import org.legstar.cobol.io.CobolOutputStream;
import org.legstar.cobol.jaxb.converter.CobolJaxbConverter;

import custdat.CustomerData;

public class CustomerDataXmlToCobol {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
                CobolOutputStream cos = new CobolOutputStream(fos)) {
            CobolJaxbConverter<CustomerData> converter = new CobolJaxbConverter<>(CustomerData.class);
            String xml = """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <customerData>
                        <customerId>1</customerId>
                        <personalData>
                            <customerName>JOHN SMITH          </customerName>
                            <customerAddress>CAMBRIDGE UNIVERSITY</customerAddress>
                            <customerPhone>44012565</customerPhone>
                        </personalData>
                        <transactions>
                            <transactionNbr>2</transactionNbr>
                            <transaction>
                                <transactionDateChoice>
                                    <transactionDate>10/05/26</transactionDate>
                                </transactionDateChoice>
                                <transactionAmount>235.56</transactionAmount>
                                <transactionComment>*********</transactionComment>
                            </transaction>
                            <transaction>
                                <transactionDateChoice>
                                    <transactionDate>13/07/26</transactionDate>
                                </transactionDateChoice>
                                <transactionAmount>-56.65</transactionAmount>
                                <transactionComment>*********</transactionComment>
                            </transaction>
                        </transactions>
                    </customerData>
                    """;
            converter.toCobol(cos, new StreamSource(new StringReader(xml))); // Serialize XML as Cobol
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```
To execute this java class the easiest thing to do is to add an assembly plugin to your pom.xml build section like so:

```xml
      <!-- Create an executable uber jar -->
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
          </descriptorRefs>
          <archive>
            <manifest>
              <mainClass>
                {mypackage}.CustomerDataXmlToCobol</mainClass> <!-- Change {mypackage} as needed -->
            </manifest>
          </archive>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

After you execute a `mvn clean package`, you should be able to execute your program with a command such as (Change {myproject} as needed) :

```
java -jar ./target/{myproject}-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
You should get a file target/CUSTDAT.bin with cobol binary data.

