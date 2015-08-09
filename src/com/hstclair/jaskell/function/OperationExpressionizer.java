package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/9/15 12:17 PM
 */
public class OperationExpressionizer<T> implements EvaluatableOperation<Object, T>, Expression<T> {

    final Operation<Object, T> operation;

    OperationExpressionizer(Operation<Object, T> operation) { this.operation = operation; }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push(operation);
    }

    @Override
    public T evaluate() {
        return performOperation(operation);
    }
}
