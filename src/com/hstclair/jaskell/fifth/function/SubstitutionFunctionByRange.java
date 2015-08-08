package com.hstclair.jaskell.fifth.function;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author hstclair
 * @since 7/17/15 9:16 PM
 */
public class SubstitutionFunctionByRange<T, R> extends FunctionImpl<T, R> {

    final Function<T, Boolean> rangeFunction;

    final Function<T, R> substituteFunction;

    public SubstitutionFunctionByRange(Function<T, Boolean> rangeFunction,
                                       Function<T, R> substituteFunction,
                                       Function<T, R> function) {
        this(rangeFunction, substituteFunction, Collections.singletonList(function));
    }

    public SubstitutionFunctionByRange(Function<T, Boolean> rangeFunction,
                                Function<T, R> substituteFunction,
                                List<Function> functions) {
        super(functions);

        Objects.requireNonNull(rangeFunction);
        Objects.requireNonNull(substituteFunction);

        this.rangeFunction = rangeFunction;
        this.substituteFunction = substituteFunction;
    }

    @Override
    Object internalApply(List<Function> functions, Object value) {
        if (rangeFunction.apply((T)value)) {
            functions.add(0, substituteFunction);
            return value;
        }

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
