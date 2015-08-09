package com.hstclair.jaskell.function;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author hstclair
 * @since 8/8/15 4:05 PM
 */
public class Selector<T, V extends Operation> implements EvaluatableOperation<T, V>, Function<T, V> {

    final Map<T, Operation> operations;

    Selector(Operation ifTrue, Operation ifFalse) {
        Map<T, Operation> operationMap = new HashMap<>();

        operationMap.put((T) Boolean.TRUE, ifTrue);
        operationMap.put((T) Boolean.FALSE, ifFalse);

        operations = Collections.unmodifiableMap(operationMap);
    }


    @Override
    public void unpack(LinkedList<Operation> operations, Object argument) {
        operations.push(this.operations::get);
    }

    @Override
    public V performOperation(T t) {
        return performOperation(this, t);
    }

    @Override
    public V apply(T t) {
        return performOperation(this, t);
    }
}
