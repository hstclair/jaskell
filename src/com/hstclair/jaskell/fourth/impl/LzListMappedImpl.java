package com.hstclair.jaskell.fourth.impl;

import com.hstclair.jaskell.fourth.LzList;

import java.util.function.Function;

/**
 * @author hstclair
 * @since 7/3/15 6:53 PM
 */
public class LzListMappedImpl<T> extends LzListImpl<T> {

    final LzListImpl<? super Object> inner;

    final Function<? super Object, T> mapper;

    public LzListMappedImpl(LzListImpl<? super Object> originalList, Function<? super Object, T> mapper) {
        inner = originalList;
        this.mapper = mapper;
    }

    @Override
    public LzListImpl<T> take(int count) {
        return null;
    }

    @Override
    public LzListImpl<T> drop(int count) {
        return null;
    }

    @Override
    public T head() {
        return null;
    }

    @Override
    public LzListImpl<T> tail() {
        return null;
    }

    @Override
    public LzListImpl<T> init() {
        return null;
    }

    @Override
    public T last() {
        return null;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public <R> LzListImpl<R> map(Function<T, R> mapper) {
        return null;
    }

    @Override
    public LzListImpl<T> reverse() {
        return null;
    }

    @Override
    public boolean isFinite() {
        return false;
    }
}
