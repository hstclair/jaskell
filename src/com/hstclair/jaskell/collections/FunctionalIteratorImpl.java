package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Expression;

import java.util.Objects;

/**
 * Implementation of FunctionalIterator using supplier expressions.  Provides a means to manipulate supplier
 * expressions before iteration.
 *
 * @author hstclair
 * @since 7/18/15 2:44 PM
 */
public class FunctionalIteratorImpl<T> implements FunctionalIterator<T> {

    /** expression providing this Iterator's hasNextExpression result */
    final Expression<Boolean> hasNextExpression;

    /** expression providing this Iterator's nextExpression member */
    final Expression<T> nextExpression;

    /**
     * Constructor
     * @param functionalIteratorStrategy the strategy object providing the getNext() expression and the
     *                                   hasNextExpression() expression to be used by this Iterator
     */
    public FunctionalIteratorImpl(FunctionalIteratorStrategy<T> functionalIteratorStrategy) {
        Objects.requireNonNull(functionalIteratorStrategy);

        this.nextExpression = functionalIteratorStrategy.getNextExpression();
        this.hasNextExpression = functionalIteratorStrategy.getHasNextExpression();
    }

    public FunctionalIteratorImpl(Expression<T> nextExpression, Expression<Boolean> hasNextExpression) {
        Objects.requireNonNull(nextExpression);
        Objects.requireNonNull(hasNextExpression);

        this.nextExpression = nextExpression;
        this.hasNextExpression = hasNextExpression;
    }

    @Override
    public boolean hasNext() {
        return hasNextExpression.evaluate();
    }

    @Override
    public T next() {
        return nextExpression.evaluate();
    }
}
