package com.hstclair.jaskell.data;

import com.hstclair.jaskell.function.Expression;
import com.hstclair.jaskell.function.Function;
import com.hstclair.jaskell.function.Indefinite;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author hstclair
 * @since 7/3/15 7:01 PM
 */
public class LzList<T> {

    public static LzList EMPTY = new LzList();

    public final Function<Integer, Indefinite<T>> getFunction;

    public final Indefinite<Integer> lengthSupplier;

    public final Expression<Boolean> isFiniteSupplier;

    private LzList() {
        getFunction = (a) -> Indefinite.EMPTY;
        lengthSupplier = () -> 0;
        isFiniteSupplier = () -> true;
    }

    public LzList(T[] values) {
        getFunction = Function.of(curriedArrayGetterFunction(values));
        lengthSupplier = () -> values.length;
        isFiniteSupplier = () -> true;
    }

    public LzList(Function<Integer, Indefinite<T>> getFunction, Indefinite<Integer> lengthExpression, Expression<Boolean> isFiniteExpression) {
        Objects.requireNonNull(getFunction);
        Objects.requireNonNull(lengthExpression);
        Objects.requireNonNull(isFiniteExpression);

        this.getFunction = getFunction;
        this.lengthSupplier = lengthExpression;
        this.isFiniteSupplier = isFiniteExpression;
    }

    public Indefinite<T> get(int index) {
        return getFunction.apply(index);
    }

    public Indefinite<T> head() {
        return get(0);
    }

    public Expression<LzList<T>> tail() {
        return () -> drop(1).evaluate();
    }

    public Expression<LzList<T>> init() {
        return () -> {                  // TODO: implement through composition
            if (! isFinite().evaluate()) return this;

            return take(length().evaluate() - 1).evaluate();
        };
    }

    public Indefinite<T> last() {
        return Indefinite.of(getFunction.apply(length().evaluate() - 1), () -> (!isFinite().evaluate()));
    }

    public Expression<LzList<T>> take(int count) {
        if (count <= 0)
            return () -> LzList.EMPTY;

        return () -> {

            Function<Integer, Indefinite<T>> newGetFunction = getFunction.substitute((Integer index) -> {
                if (index >= count)
                    return Indefinite.of(Indefinite.EMPTY);

                return Indefinite.EMPTY;
            });

            Indefinite<Integer> newLengthSupplier = Indefinite.of(lengthSupplier.andThen(value -> Math.min(value, count)).orElse(count));

            return new LzList<>(newGetFunction, newLengthSupplier, () -> true);
        };
    }

    public Expression<LzList<T>> drop(int count) {
        if (count <= 0)
            return () -> this;

        return () -> {
            if (isFinite().evaluate()) {
                if (length().evaluate() <= count) {
                    return EMPTY;
                }
            }

            return new LzList<>(getFunction.compose((value) -> value + count), lengthSupplier.andThen(length -> length - count), isFiniteSupplier);
        };
    }

    public <R> Expression<LzList<R>> map(Function<Indefinite<T>, Indefinite<R>> mapper) {
        Function<Integer, Indefinite<R>> mappedGetter = getFunction.andThen(mapper);

        return () -> new LzList<>(mappedGetter, lengthSupplier, isFiniteSupplier);
    }

    public Indefinite<Integer> length() {
        return lengthSupplier;
    }

    public Expression<Boolean> isFinite() {
        return isFiniteSupplier;
    }

    public Expression<LzList<T>> reverse() {
        return () -> new LzList<>(getFunction.compose(index -> length().evaluate() - index), lengthSupplier, isFiniteSupplier);
    }

    public Expression<Boolean> isNull() {
        return () -> (length().evaluate() <= 0);
    }

    public static <T> LzList<T> iteration(Function<T, T> generator, T seed) {
        Function<Integer, T> result = new Iteration<>(generator, seed);

        Function<Integer, Indefinite<T>> optionalGetter = result.andThen(Indefinite::of);

        return new LzList<>(optionalGetter, Indefinite.EMPTY, () -> false);
    }

    private static <T> Function<Integer, Indefinite<T>> curriedArrayGetterFunction(T[] array) {
        return Tuple.curry((BiFunction<T[], Integer, Indefinite<T>>) LzList::arrayGetter).apply(array);
    }

    public static <T> Indefinite<T> arrayGetter(T[] array, Integer index) {
        if (index < array.length)
            return Indefinite.of(array[index]);

        return Indefinite.EMPTY;
    }
}
