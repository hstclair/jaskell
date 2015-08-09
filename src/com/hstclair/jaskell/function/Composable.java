package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 8/8/15 5:50 PM
 */
interface Composable<T, R> extends Operation<T, R> {
    // TODO: Add support for composition with Expression
    default <V> Operation<V, R> composeWithInputTransformer(Operation<V, T> before) {
        Objects.requireNonNull(before);
        return new OperationList<>(before, this);
    }

}
