package com.hstclair.jaskell.function;

/**
 * @author hstclair
 * @since 8/8/15 2:58 PM
 */
@FunctionalInterface
interface Operation<T, R> {

    R performOperation(T t);
}
