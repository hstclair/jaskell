package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/9/15 1:09 PM
 */
public class OperationFunctionizer<T, R> implements EvaluatableOperation<T, R>,  Function<T, R> {

    final Operation<T, R> operation;

    OperationFunctionizer(Operation<T, R> operation) {
        this.operation = operation;
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push(operation);
    }

    @Override
    public R performOperation(T t) {
        return performOperation(operation, t);
    }

    @Override
    public R apply(T t) {
        return performOperation(operation, t);
    }
}
