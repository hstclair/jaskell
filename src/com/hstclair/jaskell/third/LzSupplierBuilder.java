package com.hstclair.jaskell.third;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/20/15 4:26 PM
 */
public interface LzSupplierBuilder<A> {

    public boolean isFinite();

    public boolean isNull();

    public int size();

    public boolean hasAtLeast(int count);

    public A get(int index);

    default public LzSupplierBuilder<A> drop(int count) {
        if (! isFinite())
            return new OffsetSupplierBuilder<A>(this, count);

        if (hasAtLeast(count))
            return new OffsetSupplierBuilder<A>(this, count);

        return LzEmptySupplierBuilder.EMPTY;
    }

    default public LzSupplierBuilder<A> take(int count) {
        if (count == 0)
            return LzEmptySupplierBuilder.EMPTY;

        if (! isFinite())
            return new OffsetSupplierBuilder<A>(this, 0, count);

        if (hasAtLeast(count))
            return new OffsetSupplierBuilder<A>(this, 0, count);

        return this;
    }
}
