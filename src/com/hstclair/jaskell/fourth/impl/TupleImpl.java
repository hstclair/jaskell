package com.hstclair.jaskell.fourth.impl;

import java.util.Optional;

/**
 * @author hstclair
 * @since 7/3/15 1:32 PM
 */
public class TupleImpl {

    final Object first;
    final Object second;
    final Object[] others;

    public TupleImpl(Object first, Object second, Object... others) {
        this.first = first;
        this.second = second;
        this.others = others;
    }

    public Object fst() {
        return first;
    }

    public Object snd() {
        return second;
    }

    public Optional<Object> get(int index) {
        if (others.length < index)
            return Optional.empty();

        return Optional.of(others[index]);
    }

    public TupleImpl swap() {
        return new TupleImpl(second, first, others);
    }
}
