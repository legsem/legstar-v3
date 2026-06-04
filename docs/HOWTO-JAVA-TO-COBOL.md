How to convert java to cobol data
=================================

Objective:
---------
Starting from the cobol copybook [CUSTDAT.cpy](../samples/CUSTDAT.cpy), convert a java bean instance into cobol binary data.

Pre-requisites:
--------------
* JDK 17 or newer
* MAVEN 3.9.11 or newer

This is a 2 steps process:
--------------------------
1. Step 1: Generate a java bean class with cobol annotations using cobol copybook CUSTDAT.cpy
2. Step 2: Execute legstar-converter to produce cobol binary data

Step 1 - Generate a java bean class with cobol annotations using CUSTDAT.cpy
----------------------------------------------------------------------------
Follow the Step 1 instructions found in [How to convert cobol data to java](./HOWTO-COBOL-TO-JAVA.md)


Step 2: Execute legstar-converter to produce a cobol binary data
----------------------------------------------------------------

1. Create a new java class named CustomerDataJavaToCobol in some package. Type the following code:

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.io.CobolOutputStream;

import custdat.CustomerData;

public class CustomerDataJavaToCobol {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("target/CUSTDAT.bin"); // Cobol binary data
                CobolOutputStream cos = new CobolOutputStream(fos)) {
            CustomerData bean = new CustomerData();
            bean.setCustomerId(new BigDecimal("245"));
            CustomerData.PersonalData personalData = new CustomerData.PersonalData();
            personalData.setCustomerName("Jean Dupond");
            personalData.setCustomerAddress("712 Hanna Plains, RoweTown");
            personalData.setCustomerPhone("12345678");
            bean.setPersonalData(personalData);
            CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
            converter.toCobol(cos, bean); // Serialize Java bean as Cobol
        } catch (IOException e) {
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
                {mypackage}.CustomerDataJavaToCobol</mainClass> <!-- Change {mypackage} as needed -->
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

