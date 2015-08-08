package com.hstclair.jaskell.first;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/18/15 9:16 PM
 */
public class LzGenerator<A> extends LzIterator<A> {

    public LzGenerator(Function<Integer, Supplier<A>> supplierFactory) {
        super(supplierFactory);
    }
}
