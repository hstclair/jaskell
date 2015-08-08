package com.hstclair.jaskell.fourth.impl;

import java.util.function.Function;

/**
 * @author hstclair
 * @since 7/2/15 8:18 PM
 */
public abstract class LzListImpl<T> {

    public final static LzListImpl EMPTY = LzListEmptyImpl.EMPTY;

    public abstract LzListImpl<T> take(int count);

    public abstract LzListImpl<T> drop(int count);

    public abstract T head();

    public abstract LzListImpl<T> tail();

    public abstract LzListImpl<T> init();

    public abstract T last();

    public abstract boolean isNull();

    public abstract int length();

    public abstract <R> LzListImpl<R> map(Function<T, R> mapper);

    public abstract LzListImpl<T> reverse();

    public abstract boolean isFinite();
}
