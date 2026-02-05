package org.legstar.cobol.generator.model;

import java.util.function.Function;

/**
 * Allows derived generators to produce more content for a generated bean.
 */
public interface RenderingOptions {
	
	boolean hasMoreImports();
	
	Function<RenderingModel, String> moreImports();

	boolean hasMoreGroupAnnotations();
	
	Function<RenderingGroup, String> moreGroupAnnotations();
	
	boolean hasMoreRootGroupAnnotations();
	
	Function<RenderingGroup, String> moreRootGroupAnnotations();
	
	static RenderingOptions defaults() {
		return new RenderingOptions() {

			@Override
			public boolean hasMoreImports() {
				return false;
			}

			@Override
			public Function<RenderingModel, String> moreImports() {
				return null;
			}

			@Override
			public boolean hasMoreGroupAnnotations() {
				return false;
			}

			@Override
			public Function<RenderingGroup, String> moreGroupAnnotations() {
				return null;
			}
			
			@Override
			public boolean hasMoreRootGroupAnnotations() {
				return false;
			}
			
			@Override
			public Function<RenderingGroup, String> moreRootGroupAnnotations() {
				return null;
			}

		};
	}

}
