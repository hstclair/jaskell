package com.hstclair.jaskell.first;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/16/15 11:44 AM
 */
public abstract class LzList<T> {

    final LzIterator<Supplier<T>> contents;

    public LzList(LzIterator<Supplier<T>> contents) {
        this.contents = contents;
    }

    public LzList<T> concat(LzList<T> list) {
        if (list instanceof FiniteLzList) {
            return new FiniteLzList<T>(head(), Arrays.asList(tail(), list ));
        }
    }

    public LzList<T> take(int count) {
        return new FiniteLzList<>()
    }

    public T head() {
        return headGetter.get();
    }


    public abstract LzList<T> tail();
}
