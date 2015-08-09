package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 7/10/15 5:57 PM
 */
class SubstitutionFunction<T, R> implements EvaluatableOperation<T, R>, Function<T, R> {

    final Function<T, R> originalFunction;
    final Function<T, Indefinite<R>> substitutionFunction;

    SubstitutionFunction(Function<T, Indefinite<R>> substitutionFunction, Function<T, R> function) {
        originalFunction = function;
        this.substitutionFunction = substitutionFunction;
    }

    @Override
    public R apply(T t) {
        return performOperation(this, t);
    }

    static <T, R> Function<Boolean, T> selectOriginalFunctionOrSubstituteExpression(LinkedList<Operation> operations, Function<T, R> originalFunction, Indefinite<R> indefinite, T argument) {
        return  (performSubstitution) -> {
            if (performSubstitution)
                operations.push(indefinite.getEvaluateExpression());    // apply the substitution expression...
            else
                operations.push(originalFunction);              // or apply the original operation

            return argument;
        };
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        // this is a sta`ck, so operations are pushed in reverse order.  That makes this extra confusing...

        operations.push((it) -> {
            Indefinite<R> indefinite = (Indefinite<R>) it;

            // 3. The Indefinite's isPresentExpression will tell us whether to...
            operations.push(selectOriginalFunctionOrSubstituteExpression(operations, originalFunction, indefinite, (T) argument));

            // 2. The second operation we wish to perform is to apply the Indefinite's isPresent expression
            operations.push(indefinite.getIsPresentExpression());

            return argument;
        });

        // 1. The first operation we are asking to perform is to apply the substitionFunction to get the Indefinite
        operations.push(substitutionFunction);
    }

    @Override
    public R performOperation(T t) {
        return performOperation(this, t);
    }
}

