package com.hstclair.jaskell.fifth.function;

/**
 * @author hstclair
 * @since 7/18/15 3:33 PM
 */
public interface Consumer<T> {

    void accept(T t);

    static <T> Consumer<T> of(java.util.function.Consumer<? super T> consumer) {
        return consumer::accept;
    }
}
