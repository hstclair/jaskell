package com.hstclair.jaskell.function;

/**
 * @author hstclair
 * @since 7/18/15 3:33 PM
 */
@FunctionalInterface
public interface Consumer<T> extends Composable<T, Void> {

    void accept(T t);

    @Override
    default Void performOperation(T t) {
        accept(t);
        return null;
    }

    default <V> Consumer<V> compose(Function<V, T> before) {
        return new OperationConsumerizer<>(composeWithInputTransformer(before));
    }

    static <T> Consumer<T> of(java.util.function.Consumer<? super T> consumer) {
        return consumer::accept;
    }
}
