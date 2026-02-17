How to convert cobol data to java
=================================

Objective:
---------
Starting from the cobol copybook [CUSTDAT.cpy](../samples/CUSTDAT.cpy), and corresponding cobol data [CUSTDAT.bin](../samples/CUSTDAT.bin), convert the cobol binary data to a java bean instance.

Pre-requisites:
--------------
* JDK 17 or newer
* MAVEN 3.9.11 or newer

This is a 2 steps process:
--------------------------
1. Step 1: Generate a java bean class with cobol annotations using cobol copybook CUSTDAT.cpy
2. Step 2: Execute legstar-converter to produce a java bean instance using cobol data CUSTDAT.bin

Step 1 - Generate a java bean class with cobol annotations using CUSTDAT.cpy
----------------------------------------------------------------------------

1. Create a maven project 
2. Create a folder src/main/cobol and copy CUSTDAT.cpy to that folder
3. Add the following dependency to your pom.xml dependencies section:

```xml
		<dependency>
			<groupId>com.legsem.legstar</groupId>
			<artifactId>legstar-converter</artifactId>
			<version>${legstar.version}</version>
		</dependency>
```

4. Add the following plugins to your pom.xml build/plugins section:

```xml
			<!-- Parses copybooks from src/main/cobol and generate java beans in target/generated-sources  -->
			<plugin>
				<groupId>com.legsem.legstar</groupId>
				<artifactId>legstar-maven-plugin</artifactId>
				<version>${legstar.version}</version>
				<executions>
					<execution>
						<phase>generate-sources</phase>
						<goals>
							<goal>generate-bean</goal>
						</goals>
						<configuration>
							<withToString>true</withToString>
						</configuration>
					</execution>
				</executions>
			</plugin>

			<!-- Add target/generated-sources as a source folder -->
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>build-helper-maven-plugin</artifactId>
				<version>3.6.1</version>
				<executions>
					<execution>
						<phase>generate-sources</phase>
						<goals>
							<goal>add-source</goal>
						</goals>
						<configuration>
							<sources>
								<source>
									${project.build.directory}/generated-sources</source>
							</sources>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

5. run `mvn clean compile`
* This command should generate the CustomerData java class in folder target/generated-sources.
* The default package name is `custdat` and compile it.

Step 2: Execute legstar-converter to produce a java bean instance using CUSTDAT.bin
-----------------------------------------------------------------------------------

1. Create a folder src/test/data and copy CUSTDAT.bin to that folder
2. Create a new java class named CustomerDataConvert in some package. Type the following code:

```java
import java.io.FileInputStream;
import java.io.IOException;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolInputStream;

import custdat.CustomerData;

public class CustomerDataConvert {

	public static void main(String[] args) {
		try (FileInputStream fis = new FileInputStream("src/test/data/CUSTDAT.bin");
				CobolInputStream cis = new CobolInputStream(fis);) {
			CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
			CustomerData bean = converter.convert(cis);
			System.out.println(bean);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
```
To execute this java class, the only requirement is to place the latest legstar-base and legstar-converter jars on the classpath (or module path).

You should get a printout of the java bean produced.
 

