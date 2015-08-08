package com.hstclair.jaskell.fourth;

import com.hstclair.jaskell.fourth.impl.TupleImpl;
import com.hstclair.jaskell.util.CachingSupplier;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 7/3/15 1:29 PM
 */
public class Tuple implements Supplier<TupleImpl> {

    final Supplier<TupleImpl> supplier;

    public Tuple(Object first, Object second, Object... others) {
        supplier = new CachingSupplier<>( () -> new TupleImpl(first, second, others));
    }

    public Supplier<Object> fst() {
        return () -> supplier.get().fst();
    }

    public Supplier<Object> snd() {
        return () -> supplier.get().snd();
    }

    public Supplier<Optional<Object>> get(Supplier<Integer> index) {
        return () -> supplier.get().get(index.get());
    }

    public Supplier<Optional<Object>> get(int index) {
        return () -> supplier.get().get(index);
    }

    public static <T, U, R> Function<T, Function<U, R>> curry(BiFunction<T, U, R> biFunction) {
        return (a) -> (b) -> biFunction.apply(a, b);
    }

    public static <T, U, R> BiFunction<T, U, R> uncurry(Function<T, Function<U, R>> curriedFunction) {
        return (a, b) -> curriedFunction.apply(a).apply(b);
    }

    public TupleImpl get() {
        return supplier.get();
    }
}
