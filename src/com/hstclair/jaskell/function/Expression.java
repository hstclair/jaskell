package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 4:15 PM
 */
@FunctionalInterface
public interface Expression<T> extends Mappable<Object, T> {

    Expression<Boolean> alwaysTrue = () -> true;

    Expression<Boolean> alwaysFalse = () -> false;

    Expression<Long> alwaysZero = () -> 0l;

    Expression<Long> alwaysOne = () -> 1l;

    Expression<Long> alwaysNegativeOne = () -> -1l;

    Expression<? super Object> alwaysNull = () -> null;

    static <T> Expression<T> of(Expression<T> expression) {
        Objects.requireNonNull(expression);

        return expression;
    }

    T evaluate();

    @Override
    default T performOperation(Object v) {
        return evaluate();
    }

    static <T> Expression<T> from(java.util.function.Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return supplier::get;
    }

    default java.util.function.Supplier<T> asSupplier() {
        return this::evaluate;
    }


    default <R> Expression<R> andThen(Function<T, R> transformation) {
        return new OperationExpressionizer<>(composeWithOutputTransformer(transformation));
    }

    default Expression<T> cache() {
        return new CachedExpression<>(this);
    }

    static <T> Expression<T> unwrap(Expression<Expression<T>> expressionExpression) {
        return () -> expressionExpression.evaluate().evaluate();
    }
}
