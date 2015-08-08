package com.hstclair.jaskell.fourth.impl;

import java.util.function.Function;

/**
 * @author hstclair
 * @since 7/2/15 8:43 PM
 */
public class LzListEmptyImpl extends LzListImpl {

    static final LzListImpl EMPTY = new LzListEmptyImpl();

    private LzListEmptyImpl() {}

    @Override
    public LzListImpl take(int count) { return EMPTY; }

    @Override
    public LzListImpl drop(int count) { return EMPTY; }

    @Override
    public Object head() {
        return null;
    }

    @Override
    public LzListImpl tail() {
        return EMPTY;
    }

    @Override
    public LzListImpl init() {
        return EMPTY;
    }

    @Override
    public Object last() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public LzListImpl reverse() {
        return EMPTY;
    }

    @Override
    public LzListImpl map(Function mapper) {
        return EMPTY;
    }

    @Override
    public boolean isFinite() {
        return true;
    }
}
