package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Function;

/**
 * Extending Iterable to add the of() generator and to introduce my extensions to the Function interface
 * @author hstclair
 * @since 7/18/15 2:23 PM
 */
public interface FunctionalIterable<T> extends Iterable<T> {    // extends iterable to apply my Function and to add the of() generator

    /**
     * Add an Until constraint to this Iteration, allowing the consumer to process all members up to, but not including,
     * the member that satisfies this predicate
     *
     * @param predicate a function which evaluates a member of the Iteration and returns <b>true</b> if it marks the
     *                  end of the Iteration
     * @return a new FunctionalIterable instance with the UntilPredicate applied
     */
    FunctionalIterable<T> takeUntil(Function<T, Boolean> predicate);

    /**
     *  Constructs a FunctionalIterable instance from a seed value and an Iterator function
     *  This results in an <i>unbounded</i> Iteration
     *
     * @param seed the Seed value that marks the start of the Iteration
     * @param generatorFunction generator function that accepts an Iteration value and returns the nextExpression Iteration value
     * @param <T> The type of the objects within the Iteration
     * @return a FunctionalIterable that may be used to traverse the members of the Iteration
     */
    static <T> FunctionalIterable<T> of(T seed, Function<T, T> generatorFunction) {
        return new FunctionalIterableImpl<>(() -> FunctionalIteratorStrategy.recursion(seed, generatorFunction));
    }
}
