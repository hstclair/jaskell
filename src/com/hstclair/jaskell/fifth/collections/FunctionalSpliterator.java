package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.function.Consumer;

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
     * @param action the Consumer that will accept values from the Iteration
     * @return
     */
    boolean tryAdvance(Consumer<? super T> action);

    @Override
    default void forEachRemaining(java.util.function.Consumer<? super T> action) {
        forEachRemaining(Consumer.of(action));
    }

    /**
     * Traverses all remaining members of this Iteration, submitting each to the specified Functional Consumer
     * @param action the Consumer that will accept values from the Iteration
     */
    void forEachRemaining(Consumer<? super T> action);
}
