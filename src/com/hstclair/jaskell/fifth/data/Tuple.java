package com.hstclair.jaskell.fifth.data;

import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Function;
import com.hstclair.jaskell.fifth.function.Indefinite;
import com.hstclair.jaskell.fifth.function.TriFunction;

import java.util.function.BiFunction;

/**
 * @author hstclair
 * @since 7/4/15 2:12 PM
 */
public class Tuple {

    Expression<Object> fstSupplier;

    Expression<Object> sndSupplier;

    Function<Integer, Indefinite<Object>> othersFunction;

    public Tuple(Object first, Object second, Object... others) {

        fstSupplier = () -> first;
        sndSupplier = () -> second;
        othersFunction = (index) -> Indefinite.of(() -> others[index], () -> (index < others.length));
    }

    public Expression<Object> fst() {
        return fstSupplier;
    }

    public Expression<Object> snd() {
        return sndSupplier;
    }

    public Indefinite<Object> get(Expression<Integer> index) {
        return this.get(index.evaluate());       // TODO: defer this accept
    }

    public Indefinite<Object> get(int index) {

        if (index == 0) return Indefinite.of(fstSupplier);
        if (index == 1) return Indefinite.of(sndSupplier);

        return othersFunction.apply(index - 2);
    }

    public static <T, U, R> Function<T, Function<U, R>> curry(BiFunction<T, U, R> biFunction) {
        return (a) -> (b) -> biFunction.apply(a, b);
    }

    public static <T, U, V, R> Function<T, Function<U, Function<V, R>>> curry(TriFunction<T, U, V, R> triFunction) {
        return (a) -> (b) -> (c) -> triFunction.apply(a, b, c);
    }

    public static <T, U, R> BiFunction<T, U, R> uncurry(Function<T, Function<U, R>> curriedFunction) {
        return (a, b) -> curriedFunction.apply(a).apply(b);
    }
}
