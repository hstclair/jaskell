package com.hstclair.jaskell.function;

import java.util.LinkedList;

/**
 * @author hstclair
 * @since 8/8/15 2:42 PM
 */
public class SubstituteExpression<T> implements EvaluatableOperation<Void, T> {

    final Expression<T> originalExpression;

    final Indefinite<T> substituteExpression;

    SubstituteExpression(Expression<T> originalExpression, Indefinite<T> substituteExpression) {
        this.originalExpression = originalExpression;
        this.substituteExpression = substituteExpression;
    }

    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push((performSubstitution)-> {
            if ((Boolean) performSubstitution) {
                operations.push(substituteExpression.getEvaluateExpression());
            } else {
                operations.push(originalExpression);
            }

            return argument;
        });

        operations.push(substituteExpression.getIsPresentExpression());
    }

    @Override
    public T performOperation(Void aVoid) {
        return performOperation(this, null);
    }
}
