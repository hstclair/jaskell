package com.hstclair.jaskell.third;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/20/15 4:19 PM
 */
public class LzArraySupplierBuilder<A> implements LzSupplierBuilder<A> {

    final A[] array;

    LzArraySupplierBuilder(A[] array) {
        if (array.length == 0)
            throw new IllegalArgumentException("Array cannot be empty");

        this.array = array;
    }

    @Override
    public A get(int index) {
        return array[index];
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public boolean isNull() {
        return array.length != 0;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean hasAtLeast(int count) {
        return count <= array.length;
    }

}
