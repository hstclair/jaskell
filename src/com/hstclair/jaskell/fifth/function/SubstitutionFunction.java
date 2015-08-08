package com.hstclair.jaskell.fifth.function;

import java.util.List;
import java.util.Objects;

/**
 * @author hstclair
 * @since 7/10/15 5:57 PM
 */
class SubstitutionFunction<T, R> extends FunctionImpl<T, R> {

    final Function<T, Indefinite<R>> substitutionFunction;

    SubstitutionFunction(Function<T, Indefinite<R>> substitutionFunction, Function<T, R> function) {
        super(function);
        this.substitutionFunction = substitutionFunction;
    }

    SubstitutionFunction(Function<T, Indefinite<R>> substitutionFunction, List<Function> functions) {
        super(functions);
        this.substitutionFunction = substitutionFunction;
    }

    public Function<T, Indefinite<R>> getSubstitutionFunction() { return substitutionFunction; }

    public List<Function> getFunctions() { return functions; }

    @Override
    Object internalApply(List<Function> functions, Object value) {
        Indefinite indefinite = substitutionFunction.apply((T) value);

        if (indefinite.isPresent())
            return indefinite.evaluate();

        return super.internalApply(functions, value);
    }

    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);

        return new FunctionImpl<V, R>((Function<V, ?>) before, this);
    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return new FunctionImpl<T, V>(this, (Function<?, V>) after);
    }
}

