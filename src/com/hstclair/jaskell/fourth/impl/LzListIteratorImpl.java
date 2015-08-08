package com.hstclair.jaskell.fourth.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author hstclair
 * @since 7/3/15 5:51 PM
 */
public class LzListIteratorImpl<T> extends LzListImpl<T> {

    final T seed;

    final int limit;

    final int offset;

    final boolean finite;

    final Function<T, T> generator;

    final List<T> resultCache;


    public LzListIteratorImpl(T seed, Function<T, T> generator) {
        this(new LinkedList<T>(), seed, generator, false, 0, -1);

        resultCache.add(seed);
    }

    public LzListIteratorImpl(List<T> resultCache, T seed, Function<T, T> generator, boolean finite, int offset, int limit) {

        this.seed = seed;
        this.generator = generator;
        this.finite = finite;
        this.offset = offset;
        this.limit = limit;
        this.resultCache = resultCache;
    }

    @Override
    public LzListImpl<T> take(int count) {
        if (count <= 0) return LzListImpl.EMPTY;

        return new LzListIteratorImpl<T>(resultCache, seed, generator, true, 0, Math.min(count, limit));
    }

    @Override
    public LzListImpl<T> drop(int count) {
        if (count <= 0) return this;
        if (finite && count >= limit) return LzListImpl.EMPTY;

        return new LzListIteratorImpl<T>(resultCache, seed, generator, finite, offset + count, finite ? limit - count : -1);
    }

    @Override
    public T head() {
        return get(0);
    }

    @Override
    public LzListImpl<T> tail() {
        return drop(1);
    }

    @Override
    public LzListImpl<T> init() {
        if (! finite) return this;

        return new LzListIteratorImpl<T>(resultCache, seed, generator, true, offset + 1, limit - 1);
    }

    @Override
    public T last() {
        if (! finite)
            return null;

        return get(limit - 1);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int length() {
        if (! finite)
            return -1;

        return limit;
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
        return finite;
    }

    public T get(int index) {
        if (offset + index == 0) return seed;

        if (finite && index >= limit) return null;

        if (resultCache.size() > offset + index)
            return resultCache.get(offset + index);

        return extend(resultCache.size() - 1 + offset + index);
    }

    private T extend(int count) {
        int lastEntry = resultCache.size() - 1;

        T newValue = resultCache.get(lastEntry);

        for (int index = 0; index < count; index++) {
            newValue = generator.apply(newValue);
            resultCache.add(newValue);
        }

        return newValue;
    }
}
