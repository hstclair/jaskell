package com.hstclair.jaskell.function;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 4:15 PM
 */
@FunctionalInterface
public interface Expression<T> {

    Expression<Boolean> alwaysTrue = () -> true;

    Expression<Boolean> alwaysFalse = () -> false;

    Expression<Long> alwaysZero = () -> 0l;

    Expression<Long> alwaysOne = () -> 1l;

    Expression<Long> alwaysNegativeOne = () -> -1l;

    Expression<? super Object> alwaysNull = () -> null;

    T evaluate();

    static <T> Expression<T> of(Expression<T> expression) {
        Objects.requireNonNull(expression);

        return expression;
    }

    static <T> Expression<T> from(java.util.function.Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return supplier::get;
    }

    default java.util.function.Supplier<T> asSupplier() {
        return this::evaluate;
    }


    default <R> Expression<R> andThen(Function<? super T, ? extends R> transformation) {
        Objects.requireNonNull(transformation);
        return new ExpressionImpl<>(this, transformation);
    }

    default Expression<T> cache() {
        return new CachedExpression<T>(this);
    }

    static <T> Expression<T> unwrap(Expression<Expression<T>> expressionExpression) {
        return () -> expressionExpression.evaluate().evaluate();
    }

    default <T> Expression<T> substitute(Indefinite<T> substitution) {
        return new SubstituteExpressionImpl<T>((Expression<T>) this, substitution);
    }

    default Expression<T> getExpression() {
        return this;
    }

    default List<Function> getTransformations() {
        return Collections.EMPTY_LIST;
    }
}