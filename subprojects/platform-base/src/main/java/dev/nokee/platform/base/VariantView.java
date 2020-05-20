package dev.nokee.platform.base;

import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;

import java.util.List;
import java.util.Set;

/**
 * A view of the variants that are created and configured as they are required.
 *
 * @param <T> type of the elements in this view
 * @since 0.2
 */
public interface VariantView<T extends Variant> {
	/**
	 * Registers an action to execute to configure each element in the view.
	 * The action is only executed for those elements that are required.
	 * Fails if any element has already been finalized.
	 *
	 * @param action The action to execute on each element for configuration.
	 */
	void configureEach(Action<? super T> action);

	/**
	 * Returns the contents of this view as a {@link Provider} of {@code <T>} instances.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @return a provider containing all the elements included in this view.
	 * @since 0.3
	 */
	Provider<Set<? extends T>> getElements();

	/**
	 * Returns a list containing the results of applying the given mapper function to each element in the view.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @param mapper a transform function to apply on each element of this view
	 * @param <S> the type of the mapped elements
	 * @return a provider containing the transformed elements included in this view.
	 * @since 0.4
	 */
	<S> Provider<List<? extends S>> map(Transformer<? extends S, ? super T> mapper);

	/**
	 * Returns a single list containing all elements yielded from results of mapper function being invoked on each element of this view.
	 *
	 * <p>The returned {@link Provider} is live, and tracks changes of the view.</p>
	 *
	 * @param mapper a transform function to apply on each element of this view
	 * @param <S> the type of the mapped elements
	 * @return a provider containing the mapped elements of this view.
	 * @since 0.4
	 */
	<S> Provider<List<? extends S>> flatMap(Transformer<Iterable<? extends S>, ? super T> mapper);
}
