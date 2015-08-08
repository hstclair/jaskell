package com.hstclair.jaskell.third;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/20/15 4:11 PM
 */
public class LzListInnerImpl<A> implements LzListInner<A> {

    final LzSupplierBuilder<A> supplierBuilder;

    public static <A> Supplier<LzListInner<A>> build(A[] array) {
        return () -> { return new LzListInnerImpl<A>(array); };
    }

    public static <A> Supplier<LzListInner<A>> build(LzSupplierBuilder<A> supplierBuilder) {
        return () -> { return new LzListInnerImpl<A>(supplierBuilder); };
    }

    public LzListInnerImpl(A[] array) {
        if (array.length == 0)
            throw new IllegalArgumentException("List cannot be empty");

        supplierBuilder = new LzArraySupplierBuilder<A>(array);
    }

    public LzListInnerImpl(LzSupplierBuilder<A> supplierBuilder) {
        if (supplierBuilder == null)
            throw new IllegalArgumentException("SupplierBuilder is null");

        if (supplierBuilder.isNull())
            throw new IllegalArgumentException("SupplierBuilder is empty");

        this.supplierBuilder = supplierBuilder;
    }

//    public Supplier<A> getSupplier(int index) {
//        if (supplierBuilder != null)
//            return supplierBuilder.get(index);
//
//        throw new IllegalStateException("No contents!");
//    }

    public final boolean isFinite() {
        return supplierBuilder.isFinite();
    }

    public boolean isNull() {
        return false;
    }

    @Override
    public Integer length() {

        if (isFinite())
            return supplierBuilder.size();

        throw new IllegalStateException("List is infinite");
    }

    @Override
    public A head() {
        return supplierBuilder.get(0);
    }

    @Override
    public A last() {

        if (isFinite())
            return supplierBuilder.get(supplierBuilder.size() - 1);

        throw new IllegalStateException("List is infinite");
    }

    @Override
    public LzListInner<A> init() {

        if (! isFinite())
            return this;

        return new LzListInnerImpl<A>(supplierBuilder.take(supplierBuilder.size() - 1));
    }

    @Override
    public LzListInner<A> tail() {
        return new LzListInnerImpl<A>(supplierBuilder.drop(1));
    }

    @Override
    public LzListInner<A> take(int length) {
        return new LzListInnerImpl<A>(supplierBuilder.take(length));
    }

    @Override
    public LzListInner<A> drop(int length) {
        return new LzListInnerImpl<A>(supplierBuilder.drop(length));
    }

    @Override
    public LzListInner<A> concatenate(LzListInner<A> list) {
        throw new NotImplementedException();
    }
}
