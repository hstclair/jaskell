package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/8/15 1:53 PM
 */
interface EvaluatableOperation<T, R> extends Operation<T, R> {
    void unpack(LinkedList<Operation> operations, Object argument);

    default R performOperation(Operation<T, R> operation) {
        return performOperation(operation, null);
    }

    default R performOperation(Operation<T, R> operation, T t) {
        Evaluator<T, R> evaluator = new Evaluator<>();

        return evaluator.evaluate(operation, t);
    }
}
