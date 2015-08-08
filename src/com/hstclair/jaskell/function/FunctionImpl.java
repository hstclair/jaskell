package com.hstclair.jaskell.function;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 3:30 PM
 */
public class FunctionImpl<T, R> implements Function<T, R> {

    final List<Function> functions;

    public FunctionImpl(Function<T, R> function) {
        List<Function> functionList = new LinkedList<>();

        addToFunctions(function, functionList);

        functions = Collections.unmodifiableList(functionList);
    }

    public FunctionImpl(List<Function> functions) {
        this.functions = Collections.unmodifiableList(functions);
    }

    public FunctionImpl(Function<T, ?> before, Function<?, R> after) {
        List<Function> functionList = new LinkedList<>();

        addToFunctions(before, functionList);
        addToFunctions(after, functionList);

        functions = Collections.unmodifiableList(functionList);
    }

    FunctionImpl(Function<T, ?> before, List<Function> after) {
        List<Function> functionList = new LinkedList<Function>();

        addToFunctions(before, functionList);

        functionList.addAll(after);

        functions = Collections.unmodifiableList(functionList);
    }

    FunctionImpl(List<Function> before, Function<?, R> after) {
        List<Function> functionList = new LinkedList<>();

        functionList.addAll(before);

        addToFunctions(after, functionList);

        functions = Collections.unmodifiableList(functionList);
    }

    static void addToFunctions(Function function, List<Function> functions) {
        Objects.requireNonNull(function);

        if (function.getClass() == FunctionImpl.class)
            functions.addAll(((FunctionImpl) function).functions);
        else
            functions.add(function);
    }

    Object internalApply(List<Function> functions, Object value) {
        functions.addAll(0, this.functions);

        return value;
    }

    Object applyFunction(List<Function> functions, Object value) {

        Function<Object, Object> function = functions.remove(0);

        if (function instanceof FunctionImpl)
            return ((FunctionImpl) function).internalApply(functions, value);

        if (function != null)
            return function.apply(value);

        return value;
    }

    R applyImpl(Function<T, R> firstFunction, T value) {

        LinkedList<Function> functions = new LinkedList<>();
        functions.add(firstFunction);

        Object result = value;

        while (! functions.isEmpty()) {
            result = applyFunction(functions, result);
        }

        return (R) result;
    }

    @Override
    public R apply(T t) {
        return applyImpl(this, t);
    }

    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);

        return new FunctionImpl<V, R>((Function<V, ?>) before, functions);
    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return new FunctionImpl<T, V>(functions, (Function<?, V>) after);
    }
}
