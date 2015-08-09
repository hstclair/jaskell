package com.hstclair.jaskell.function;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 7/18/15 10:14 PM
 */
public class OperationList<T, R> implements Operation<T, R>, EvaluatableOperation<T, R> {

    final List<Operation> operations;

    public OperationList(List<Operation> operations) {
        this.operations = Collections.unmodifiableList(operations);
    }

    public OperationList(Operation<T, ?> before, Operation<?, R> after) {
        List<Operation> operations = new LinkedList<>();

        appendToList(operations, before);
        appendToList(operations, after);
        this.operations = Collections.unmodifiableList(operations);
    }

    static List<Operation> appendToList(List<Operation> operations, Operation operation) {
        if (operation.getClass() == OperationList.class)
            operations.addAll(((OperationList) operation).operations);
        else
            operations.add(operation);

        return operations;
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.addAll(0, this.operations);
    }

    @Override
    public R performOperation(T t) {
        Evaluator<T, R> evaluator = new Evaluator<>();

        return evaluator.evaluate(this, t);
    }
}
