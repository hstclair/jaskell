package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 3:25 PM
 */
@FunctionalInterface
public interface Function<T, R> {

    static <T> Function<T, T> identity() { return it->it; }

    static <T, R> Function<T, R> of(Function<T, R> function) {
        Objects.requireNonNull(function);

        return function;
    }

    static <T, R> Function<T, R> from(java.util.function.Function<T, R> javaFunction) {
        return javaFunction::apply;
    }

    default java.util.function.Function<T, R> asJavaFunction() {
        return this::apply;
    }

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return new FunctionImpl<V, R>((Function<V, ?>) before, this);
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);

        return new FunctionImpl<T, V>(this, (Function<?, V>) after);
    }

    default Function<T, R> substitute(Function<T, Indefinite<R>> substitutionFunction) {
        Objects.requireNonNull(substitutionFunction);

        return new SubstitutionFunction<>(substitutionFunction, this);
    }

    default Function<T, R> substitute(Function<T, Boolean> range, Function<T, R> substitute) {
        Objects.requireNonNull(range);
        Objects.requireNonNull(substitute);

        return new SubstitutionFunctionByRange<T, R>(range, substitute, this);
    }

    R apply(T t);
}
