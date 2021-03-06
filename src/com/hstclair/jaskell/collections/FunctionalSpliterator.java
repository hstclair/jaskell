package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Consumer;

import java.util.Spliterator;

/**
 * adapter between Java Spliterator and functional classes
 * @author hstclair
 * @since 7/18/15 2:39 PM
 */
public interface FunctionalSpliterator<T> extends Spliterator<T> {

    @Override
    default boolean tryAdvance(java.util.function.Consumer<? super T> action) {
        return tryAdvance(Consumer.of(action));
    }

    /**
     * performs a tryAdvance() operation and supplies results to a Functional Consumer
     * @param action the Consumer that will apply values from the Iteration
     * @return true if the operation succeeded (a value was found and provided to the Consumer)
     */
    boolean tryAdvance(Consumer<? super T> action);

    @Override
    default void forEachRemaining(java.util.function.Consumer<? super T> action) {
        forEachRemaining(Consumer.of(action));
    }

    /**
     * Traverses all remaining members of this Iteration, submitting each to the specified Functional Consumer
     * @param action the Consumer that will apply values from the Iteration
     */
    void forEachRemaining(Consumer<? super T> action);
}
