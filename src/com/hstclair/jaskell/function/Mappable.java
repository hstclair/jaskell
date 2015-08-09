package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 8/8/15 5:56 PM
 */
interface Mappable<T, R> extends Operation<T, R> {
    // TODO: Add support for andThen Consumer
    default <V> Operation<T, V> composeWithOutputTransformer(Operation<R, V> after) {
        Objects.requireNonNull(after);

        return new OperationList<>(this, after);
    }

}
