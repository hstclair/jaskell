package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 8/1/15 12:56 PM
 */
public class RecursionExpression<T> implements Expression<T> {

    /** the function that transforms each member of the Iteration into the member that follows it */
    public final Function<T, T> generatorFunction;

    /** the current value in the Iteration */
    public T current;

    public RecursionExpression(T seed, Function<T, T> generatorFunction) {
        Objects.requireNonNull(seed);
        Objects.requireNonNull(generatorFunction);

        this.generatorFunction = generatorFunction;
        this.current = seed;
    }

    @Override
    public T evaluate() {
        T result = current;
        current = generatorFunction.apply(current);
        return result;
    }
}
