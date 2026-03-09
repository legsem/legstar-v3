package org.legstar.cobol.maven.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MojoTest
public class JaxbGeneratorMojoTest {

	@BeforeEach
	public void setUp() {
		File outputDirectory = new File("target/generated-sources/java");
		clear(outputDirectory);
	}

	@Test
	@InjectMojo(goal = "generate-jaxb")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	public void testMojoNoSource(JaxbGeneratorMojo mojo) throws Exception {
		try {
			mojo.execute();
			fail();
		} catch (MojoExecutionException | MojoFailureException e) {
			assertEquals(
					"Missing a source parameter. This should point to a COBOL copybook file or a directory containing COBOL copybook files.",
					e.getMessage());
		}
	}

	@Test
	@InjectMojo(goal = "generate-jaxb")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	@MojoParameter(name = "source", value = "src/test/cobol")
	public void testMojoDirectory(JaxbGeneratorMojo mojo) throws Exception {
		mojo.execute();
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/Flat01Record.java").exists());
	}

	@Test
	@InjectMojo(goal = "generate-jaxb")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	@MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
	public void testMojoFile(JaxbGeneratorMojo mojo) throws Exception {
		mojo.execute();
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/Flat01Record.java").exists());
	}

	@Test
	@InjectMojo(goal = "generate-jaxb")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	@MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
	@MojoParameter(name = "rootXmlNamespace", value = "http://org.legstar/flat01")
	public void testRootXmlNamespace(JaxbGeneratorMojo mojo) throws Exception {
		mojo.execute();
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/Flat01Record.java").exists());
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/package-info.java").exists());
	}

	private void clear(File dir) {
		if (dir.exists()) {
			try (Stream<Path> pathStream = Files.walk(dir.toPath())) {
				pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			dir.delete();
		}
	}

}
