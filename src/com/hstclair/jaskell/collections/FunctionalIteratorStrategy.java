package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Expression;
import com.hstclair.jaskell.function.Function;
import com.hstclair.jaskell.function.RecursionExpression;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/18/15 9:06 PM
 */
public class FunctionalIteratorStrategy<T> {

    Expression<T> nextExpression;

    Expression<Boolean> hasNextExpression;

    protected FunctionalIteratorStrategy() { }

    public FunctionalIteratorStrategy(Expression<T> nextExpression) {
        this(nextExpression, Expression.alwaysTrue);
    }

    public FunctionalIteratorStrategy(Expression<T> nextExpression, Expression<Boolean> hasNextExpression) {
        Objects.requireNonNull(nextExpression);
        Objects.requireNonNull(hasNextExpression);

        this.nextExpression = nextExpression;
        this.hasNextExpression = hasNextExpression;
    }

    public Expression<Boolean> getHasNextExpression() { return hasNextExpression; }

    public Expression<T> getNextExpression() {
        return nextExpression;
    }

    static <T> FunctionalIteratorStrategy<T> recursion(T seed, Function<T, T> generator) {
        return new FunctionalIteratorStrategy<>(new RecursionExpression<>(seed, generator), Expression.alwaysTrue);
    }

    public FunctionalIteratorStrategy<T> until(Function<T, Boolean> predicate) {
        return new FunctionalIteratorUntilStrategy<>(this, predicate);
    }
}
