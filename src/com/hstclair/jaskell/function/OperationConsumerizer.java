package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/9/15 11:44 AM
 */
public class OperationConsumerizer<T> implements EvaluatableOperation<T, Void>, Consumer<T> {

    final Operation<T, Void> operation;

    OperationConsumerizer(Operation<T, Void> operation) {
        this.operation = operation;
    }

    @Override
    public void accept(T t) {
        performOperation(operation, t);
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push(operation);
    }
}
