package com.hstclair.jaskell.third;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/20/15 4:05 PM
 */
public class LzEmptyInner implements LzListInner {

    public static final LzEmptyInner EMPTY = new LzEmptyInner();

    private LzEmptyInner() {}

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
    public LzListInner init() {
        return this;
    }

    @Override
    public LzListInner tail() {
        return this;
    }

    @Override
    public LzListInner take(int length) {
        return this;
    }

    @Override
    public LzListInner drop(int length) {
        return this;
    }

    @Override
    public LzListInner concatenate(LzListInner list) {
        return list;
    }
}
