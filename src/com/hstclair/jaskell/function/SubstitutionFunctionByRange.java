package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 7/17/15 9:16 PM
 */
public class SubstitutionFunctionByRange<T, R> implements EvaluatableOperation<T, R>, Function<T, R> {

    final Function<T, Boolean> rangeFunction;

    final Function<T, R> substituteFunction;

    final Function<T, R> originalFunction;

    public SubstitutionFunctionByRange(Function<T, Boolean> rangeFunction,
                                       Function<T, R> substituteFunction,
                                       Function<T, R> originalFunction) {
        this.rangeFunction = rangeFunction;
        this.substituteFunction = substituteFunction;
        this.originalFunction = originalFunction;
    }

    @Override
    public R apply(T t) {
        return performOperation(this, t);
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push((performSubstitution) -> {

            if ((Boolean) performSubstitution)
                operations.push(substituteFunction);
            else
                operations.push(originalFunction);

            return argument;
        });

        operations.push(rangeFunction);
    }

    @Override
    public R performOperation(T t) {
        return performOperation(this, t);
    }
}
