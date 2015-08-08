package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/5/15 9:55 PM
 */
public class CachedExpression<T> implements Expression<T> {

    private volatile boolean present;
    private volatile T cachedValue;

    private Expression<T> expression;

    CachedExpression(Expression<T> expression) {
        Objects.requireNonNull(expression);
        present = false;
        this.expression = expression;
    }

    @Override
    public T evaluate() {
        if (! present) {
            synchronized (this) {
                if (! present) {
                    cachedValue = expression.evaluate();
                    present = true;
                    expression = null;
                }
            }
        }

        return cachedValue;
    }
}
