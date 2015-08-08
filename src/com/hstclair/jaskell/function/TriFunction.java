package com.hstclair.jaskell.function;

/**
 * @author hstclair
 * @since 7/11/15 5:31 PM
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
