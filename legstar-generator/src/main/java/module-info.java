module org.legstar.cobol.generator {
	
	requires transitive org.legstar.cobol.base;
	requires transitive org.legstar.cobol.parser;
	requires transitive gg.jte.runtime;
	
	exports org.legstar.cobol.generator;
	exports org.legstar.cobol.generator.model;
	exports gg.jte.generated.precompiled;

}