package com.hstclair.jaskell.third;

import com.hstclair.jaskell.util.CachingSupplier;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/20/15 5:00 PM
 */
public class OffsetSupplierBuilder<A> implements LzSupplierBuilder<A> {

    final LzSupplierBuilder<A> supplierBuilder;

    final int offset;

    final Supplier<Integer> sizeSupplier;

    OffsetSupplierBuilder(LzSupplierBuilder<A> supplierBuilder, int offset, int size) {
        this.supplierBuilder = supplierBuilder;

        if (offset < 0)
            throw new IllegalArgumentException("negative offset");

        if (size <= 0)
            throw new IllegalArgumentException("negative or zero size");

        if (! supplierBuilder.hasAtLeast(offset + 1))
            throw new IllegalArgumentException("Empty SupplierBuilder after offset");

        this.offset = offset;

        if (supplierBuilder.hasAtLeast(offset + size)) {
            this.sizeSupplier = () -> { return size; };
        } else {
            this.sizeSupplier = new CachingSupplier<>(this::supplierBuilderSize);
        }
    }

    OffsetSupplierBuilder(LzSupplierBuilder<A> supplierBuilder, int offset) {
        this.supplierBuilder = supplierBuilder;
        this.offset = offset;

        sizeSupplier = new CachingSupplier<>(this::supplierBuilderSize);
    }

    @Override
    public boolean isFinite() {
        return supplierBuilder.isFinite();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return sizeSupplier.get();
    }

    private int supplierBuilderSize() {

        if (supplierBuilder.isFinite())
            return supplierBuilder.size() - offset;

        throw new IllegalArgumentException();
    }


    @Override
    public boolean hasAtLeast(int count) {
        return count <= sizeSupplier.get(); // TODO: can we use hasAtLeast() instead?  Are we better off calling length()?
    }

    @Override
    public A get(int index) {
        if (index < sizeSupplier.get())     // TODO: can we avoid the call to length here?
            return supplierBuilder.get(index + offset);

        throw new IllegalArgumentException("index out of range");
    }
}
