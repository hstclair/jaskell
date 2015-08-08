package com.hstclair.jaskell.first;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/18/15 9:22 PM
 */
public class LzFiniteIterator<A> extends LzIterator<A> {

    final int size;

    final int offset;

    public LzFiniteIterator(Function<Integer, Supplier<A>> supplierFactory, int offset, int size) {
        super(supplierFactory);

        if (offset < 0) throw new IllegalArgumentException("Offset must not be negative");
        if (size < 0) throw new IllegalArgumentException("Size must not be negative");

        this.offset = offset;
        this.size = size;
    }

    @Override
    public LzIterator<A> drop(int count) {
        if (count >= size)
            return EMPTY;

        return super.drop(offset + count).take(size - count);
    }

    @Override
    public LzIterator<A> take(int count) {
        if (count <= 0)
            return EMPTY;

        if (size == 0)
            return EMPTY;

        return super.drop(offset).take(Math.min(count, size));
    }
}
