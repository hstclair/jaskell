package com.hstclair.jaskell.first;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/18/15 8:45 PM
 */
public abstract class LzIterator<A> {

    public static final LzIterator EMPTY = new LzFiniteIterator<>(null, 0, 0);

    final Function<Integer, Supplier<A>> supplierFactory;

    protected LzIterator(Function<Integer, Supplier<A>> supplierFactory) {
        this.supplierFactory = supplierFactory;
    }


    public LzIterator<A> take(int count) {
        if (count <= 0) return EMPTY;

        return new LzFiniteIterator<A>(supplierFactory, 0, count);
    }


    public LzIterator<A> drop(int count) {
        return new LzFiniteIterator<A>(supplierFactory, count, 0);
    }

    public Function<Integer, Supplier<A>> getSupplierFactory() { return supplierFactory; }

//    private Function<Integer, Supplier<Function<Integer, Supplier<A>>>> supplierSupplier() {
//        return (index) -> {
//            if (index == 0)
//                return () -> { return supplierFactory; };
//
//            return () -> { return null; };
//        };
//    }
//
//    protected LzIterator<Function<Integer, Supplier<A>>> getSuppliers() {
//        return new LzFiniteIterator<Function<Integer, Supplier<A>>>(supplierSupplier(), 0, 1);
//    }
}
