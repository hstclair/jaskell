package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/8/15 12:09 PM
 */
public class Evaluator<T, R> {

    public R evaluate(Expression expression) {
        return evaluate(expression, null);
    }

    public R evaluate(Operation initialOperation, T t) {
        LinkedList<Operation<Object, Object>> operations = new LinkedList<>();

        operations.push(initialOperation);

        Object argument = t;

        while (operations.size() != 0) {

            Operation<Object, Object> operation = operations.pop();

            if (operation instanceof EvaluatableOperation)
                ((EvaluatableOperation) operation).unpack(operations, argument);
            else {
                argument = operation.performOperation(argument);
            }
        }

        return (R) argument;
    }
}
