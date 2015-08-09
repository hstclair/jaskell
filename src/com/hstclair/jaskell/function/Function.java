package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 3:25 PM
 */
@FunctionalInterface
public interface Function<T, R> extends Composable<T, R>, Mappable<T, R> {

    static <T> Function<T, T> identity() { return it->it; }

    static <T, R> Function<T, R> of(Function<T, R> function) {
        Objects.requireNonNull(function);

        return function;
    }

    static <T, R> Function<T, R> from(java.util.function.Function<T, R> javaFunction) {
        return javaFunction::apply;
    }

    R apply(T t);

    default java.util.function.Function<T, R> asJavaFunction() {
        return this::apply;
    }

    default <V> Function<T, V> andThen(Function<R, V> after) {
        return new OperationFunctionizer<>(composeWithOutputTransformer(after));
    }

    default <V> Function<V, R> compose(Function<V, T> before) {
        return new OperationFunctionizer<>(composeWithInputTransformer(before));
    }

    default Function<T, R> substitute(Function<T, Indefinite<R>> substitutionFunction) {
        Objects.requireNonNull(substitutionFunction);

        return new SubstitutionFunction<>(substitutionFunction, this);
    }

    default Function<T, R> substitute(Function<T, Boolean> range, Function<T, R> substitute) {
        Objects.requireNonNull(range);
        Objects.requireNonNull(substitute);

        return new SubstitutionFunctionByRange<>(range, substitute, this);
    }

    @Override
    default R performOperation(T t) {
        return apply(t);
    }
}
