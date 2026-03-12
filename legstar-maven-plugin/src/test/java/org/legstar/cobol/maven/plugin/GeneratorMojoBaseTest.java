package org.legstar.cobol.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class GeneratorMojoBaseTest {

	public void clear(File dir) {
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
