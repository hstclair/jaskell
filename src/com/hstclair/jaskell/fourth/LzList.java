package com.hstclair.jaskell.fourth;

import com.hstclair.jaskell.fourth.impl.LzListArrayImpl;
import com.hstclair.jaskell.fourth.impl.LzListImpl;
import com.hstclair.jaskell.util.CachingSupplier;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 7/2/15 8:30 PM
 */
public class LzList<T> implements Supplier<LzListImpl<T>> {

    Supplier<LzListImpl<T>> supplier;

    public LzList(T[] array) {
        supplier = new CachingSupplier<>(() -> new LzListArrayImpl<T>(array));
    }

    public LzList(LzList<T> list) {
        supplier = list.supplier;
    }

    public LzList(Supplier<LzListImpl<T>> listSupplier) {
        supplier = new CachingSupplier<>(listSupplier);
    }

    @Override
    public LzListImpl<T> get() {
        return supplier.get();
    }

    public LzList<T> take(int count) {
        return take(() -> count);
    }

    public LzList<T> take(Supplier<Integer> count) {
        return new LzList<T>( () -> supplier.get().take(count.get()));
    }

    public LzList<T> drop(int count) {
        return drop(() -> count);
    }

    public LzList<T> init() {
        return new LzList<T>( () -> supplier.get().init());
    }

    public LzList<T> drop(Supplier<Integer> count) {
        return new LzList<T>( () -> supplier.get().drop(count.get()));
    }

    public LzList<T> tail() {
        return new LzList<T>( () -> supplier.get().tail());
    }

    public Supplier<T> head() {
        return () -> supplier.get().head();
    }

    public Supplier<T> last() {
        return () -> supplier.get().last();
    }

    public Supplier<Boolean> isNull() {
        return () -> supplier.get().isNull();
    }

    public Supplier<Integer> length() {
        return () -> supplier.get().length();
    }

    public <R> LzList<R> map(Function<T, R> mapper) {
        return new LzList<R>(() -> supplier.get().map(mapper));
    }
}
