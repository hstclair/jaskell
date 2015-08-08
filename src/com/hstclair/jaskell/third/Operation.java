package com.hstclair.jaskell.third;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/23/15 9:19 PM
 */
public abstract class Operation<R> {
// TODO: what if an element is either an Operation or a Value and the program's purpose is to transform all Operations into Values???

    private volatile R cachedValue;

    public abstract List<Operation> getArguments();

    protected abstract R evaluate();

    public R get() {
        if (cachedValue == null) {
            synchronized (this) {
                if (cachedValue == null) {
                    cachedValue = evaluate();


                }
            }
        }

        return null;
    }
}
