package com.hstclair.jaskell.third;

/**
 * @author hstclair
 * @since 6/20/15 5:09 PM
 */
public class LzEmptySupplierBuilder implements LzSupplierBuilder {

    public static final LzSupplierBuilder EMPTY = new LzEmptySupplierBuilder();

    private LzEmptySupplierBuilder() {}

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean hasAtLeast(int count) {
        return count == 0;
    }

    @Override
    public Object get(int index) {
        throw new IllegalArgumentException("Empty Supplier");
    }
}
