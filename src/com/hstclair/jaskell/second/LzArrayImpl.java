package com.hstclair.jaskell.second;

/**
 * @author hstclair
 * @since 6/20/15 9:30 AM
 */
public class LzArrayImpl<A> implements LzList<A> {

    final A[] array;

    final int offset;

    final int size;

    LzArrayImpl(A[] array) {
        this(array, 0, array.length);
    }

    LzArrayImpl(A[] array, int offset) {
        this(array, Math.min(offset, array.length), array.length);
    }

    LzArrayImpl(A[] array, int offset, int size) {
        this.array = array;
        this.offset = offset;
        this.size = Math.min(size, array.length - offset);

        if (size == 0)
            throw new IllegalArgumentException("List cannot be empty");
    }

    public boolean isFinite() { return true; }

    public boolean isNull() { return false; }

    @Override
    public Integer length() {
        return size;
    }

    public A head() {
        return array[0];
    }

    public A last() {
        return array[offset + size - 1];
    }

    @Override
    public LzList<A> init() {
        return new LzArrayImpl<A>(array, 0, size - 1);
    }

    public LzList<A> tail() {
        if (size == 1) return LzList.EMPTY;

        return new LzArrayImpl<A>(array, 1);
    }

    @Override
    public LzList<A> take(int length) {
        return null;
    }

    @Override
    public LzList<A> drop(int length) {
        return null;
    }

    @Override
    public LzList<A> concatenate(LzList<A> list) {
        return null;
    }

}
