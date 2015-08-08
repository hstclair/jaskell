package com.hstclair.jaskell.second;

import sun.plugin.dom.exception.InvalidStateException;

/**
 * @author hstclair
 * @since 6/20/15 9:42 AM
 */
public class LzEmpty implements LzList {

    public static final LzEmpty EMPTY = new LzEmpty();

    public LzEmpty() {}

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Integer length() {
        return 0;
    }

    @Override
    public Object head() {
        throw new InvalidStateException("EMPTY list has no head");
    }

    @Override
    public Object last() {
        throw new InvalidStateException("EMPTY list");
    }

    @Override
    public LzList init() {
        return this;
    }

    @Override
    public LzList tail() {
        return this;
    }

    @Override
    public LzList concatenate(LzList list) {
        return list;
    }
}
