package com.hstclair.jaskell.fourth.impl;

import java.util.function.Function;

/**
 * @author hstclair
 * @since 7/2/15 8:40 PM
 */
public class LzListArrayImpl<T> extends LzListImpl<T> {

    final Object[] content;
    final int offset;
    final int size;
    final boolean reversed;
    final Function<Object, T> mapper;

    public LzListArrayImpl(T[] content) {
        this(content, 0, content.length, (Function<Object, T>) Function.identity(), false);
    }

    public LzListArrayImpl(Object[] content, int offset, Function<? super Object, T> mapper, boolean reversed) {
        this(content, offset, content.length - offset, mapper, reversed);
    }

    public LzListArrayImpl(Object[] content, int offset, int size, Function<? super Object, T> mapper, boolean reversed) {
        if (content.length == 0) throw new IllegalArgumentException("Construction from empty array");
        if (size == 0) throw new IllegalArgumentException("Construction with zero size");
        if (content.length <= offset) throw new IllegalArgumentException("Construction with complete offset");


        this.content = content;
        this.offset = offset;
        this.size = Math.min(size, content.length - offset);
        this.mapper = mapper;
        this.reversed = reversed;
    }

    private <Q> T mapperHelper(Q x) {
        return mapper.apply(x);
    }

    @Override
    public LzListImpl<T> take(int count) {
        if (count <= 0) return LzListImpl.EMPTY;

        return new LzListArrayImpl<T>(content, 0, Math.min(count, size), mapper, reversed);
    }

    @Override
    public LzListImpl<T> drop(int count) {
        if (count <= 0) return this;
        if (count >= size) return LzListImpl.EMPTY;

        return new LzListArrayImpl<T>(content, count, mapper, reversed);
    }

    @Override
    public T head() {
        return mapper.apply(content[0]);
    }

    @Override
    public LzListImpl<T> tail() {
        if (size <= 1) return LzListImpl.EMPTY;

        return new LzListArrayImpl<T>(content, 1, mapper, reversed);
    }

    @Override
    public LzListImpl<T> init() {
        if (size == 1) return LzListImpl.EMPTY;

        return new LzListArrayImpl<T>(content, 0, content.length - 1, mapper, reversed);
    }

    @Override
    public T last() {
        return mapper.apply(content[size-1]);
    }

    @Override
    public boolean isNull() {
        return false;   // if code is correct, this will never be empty - instead, it will be replaced by LzListImpl.EMPTY
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public <R> LzListImpl<R> map(Function<T, R> mapper) {
        return new LzListArrayImpl<R>(content, offset, size, this.mapper.andThen(mapper), reversed);
    }

    @Override
    public LzListImpl<T> reverse() {
        return new LzListArrayImpl<T>(content, offset, size, this.mapper, ! reversed);
    }

    @Override
    public boolean isFinite() {
        return true;
    }
}
