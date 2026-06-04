How to convert JSON to cobol data
=================================

Objective:
---------
Starting from the cobol copybook [CUSTDAT.cpy](../samples/CUSTDAT.cpy), convert a JSON instance into cobol binary data.

Pre-requisites:
--------------
* JDK 17 or newer
* MAVEN 3.9.11 or newer

This is a 2 steps process:
--------------------------
1. Step 1: Generate a java bean class with cobol and JACKSON annotations using cobol copybook CUSTDAT.cpy
2. Step 2: Execute legstar-json-converter to produce cobol binary data

Step 1 - Generate a java bean class with cobol and JACKSON annotations using CUSTDAT.cpy
----------------------------------------------------------------------------------------
Follow the Step 1 instructions found in [How to convert cobol data to JSON](./HOWTO-COBOL-TO-JSON.md)


Step 2: Execute legstar-json-converter to produce a cobol binary data
---------------------------------------------------------------------

1. Create a new java class named CustomerDataJsonToCobol in some package. Type the following code:

```java
import java.io.FileOutputStream;
import java.io.StringReader;

import org.legstar.cobol.io.CobolOutputStream;
import org.legstar.cobol.json.converter.CobolJsonConverter;

import custdat.CustomerData;

public class CustomerDataJsonToCobol {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
                CobolOutputStream cos = new CobolOutputStream(fos)) {
            CobolJsonConverter<CustomerData> converter = new CobolJsonConverter<>(CustomerData.class);
            String json = """
                    {
                        "customerId": 2,
                        "personalData": {
                            "customerName": "FRED BROWN",
                            "customerAddress": "CAMBRIDGE",
                            "customerPhone": "38791206"
                        },
                        "transactions": {
                            "transactionNbr": 2,
                            "transaction": [
                                {
                                    "transactionDateChoice": {
                                        "transactionDate": "10/05/26"
                                    },
                                    "transactionAmount": 36.82,
                                    "transactionComment": "*********"
                                },
                                {
                                    "transactionDateChoice": {
                                        "transactionDate": "13/07/26"
                                    },
                                    "transactionAmount": 175.93,
                                    "transactionComment": "*********"
                                }
                            ]
                        }
                    }
            """;
            converter.toCobol(cos, new StringReader(json));
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
                {mypackage}.CustomerDataJsonToCobol</mainClass> <!-- Change {mypackage} as needed -->
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

