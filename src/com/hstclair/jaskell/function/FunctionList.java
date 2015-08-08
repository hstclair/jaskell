package com.hstclair.jaskell.function;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author hstclair
 * @since 7/18/15 10:14 PM
 */
public class FunctionList<T, R> implements Function<T, R> {

    final List<Function> functions;

    public FunctionList(Function<T, R> function) {
        List<Function> functions = new LinkedList<>();
        appendToList(functions, function);

        this.functions = Collections.unmodifiableList(functions);
    }

    public FunctionList(List<Function> functions) {
        this.functions = Collections.unmodifiableList(functions);
    }

    public FunctionList(Function<T, ?> before, Function<?, R> after) {
        List<Function> functions = new LinkedList<>();

        appendToList(functions, before);
        appendToList(functions, after);
        this.functions = Collections.unmodifiableList(functions);
    }

    static List<Function> appendToList(List<Function> functions, Function function) {
        if (function.getClass() == FunctionList.class)
            functions.addAll(((FunctionList) function).functions);
        else
            functions.add(function);

        return functions;
    }

    // TODO: Turn these objects into data structures that are decorated with methods.  Do the real work in a VISITOR!!!

    @Override
    public R apply(T t) {
        Object value = t;

        for (Function function : functions) {
            value = function.apply(value);
        }

        return (R) value;
    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);

        return new FunctionList<>(this, (FunctionList<R, V>) after);
    }

    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);

        return new FunctionList<>((Function<V, T>) before, this);
    }
}
