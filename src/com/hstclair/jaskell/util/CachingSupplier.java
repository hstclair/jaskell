package com.hstclair.jaskell.util;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/21/15 7:03 PM
 */
public class CachingSupplier<A> implements Supplier<A> {

    volatile A value = null;

    final Supplier<A> inner;

    public CachingSupplier(Supplier<A> inner) {
        if (inner == null)
            throw new IllegalArgumentException("inner supplier cannot be null");

        this.inner = inner;
    }

    @Override
    final public A get() {

        if (value == null) {
            synchronized (this) {
                if (value == null)
                    value = inner.get();
            }
        }

        return value;
    }
}
