package org.legstar.cobol.generator.model;

import java.util.function.Function;

/**
 * Allows derived generators to produce more content for a generated bean.
 */
public interface RenderingOptions {

	/**
	 * Whether the generated java bean should include more imports.
	 * 
	 * @return true if the generated java bean should include more imports
	 */
	boolean hasMoreImports();

	/**
	 * An additional imports provider.
	 * 
	 * @return a method that renders additional imports to include in the generated
	 *         java bean.
	 */
	Function<RenderingModel, String> moreImports();

	/**
	 * Whether the generated java bean should include additional annotations for
	 * group items.
	 * 
	 * @return true if the generated java bean should more annotations for group
	 *         items
	 */
	boolean hasMoreGroupAnnotations();

	/**
	 * An additional group annotations provider.
	 * 
	 * @return a method that renders additional group annotations to include in the
	 *         generated java bean.
	 */
	Function<RenderingGroup, String> moreGroupAnnotations();

	/**
	 * Whether the generated java bean should include additional annotations for
	 * root item.
	 * 
	 * @return true if the generated java bean should more annotations for the root
	 *         item
	 */
	boolean hasMoreRootGroupAnnotations();

	/**
	 * An additional root annotations provider.
	 * 
	 * @return a method that renders additional root annotations to include in the
	 *         generated java bean.
	 */
	Function<RenderingGroup, String> moreRootGroupAnnotations();

	/**
	 * A default implementation.
	 * 
	 * @return the default set of rendering options
	 */
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
