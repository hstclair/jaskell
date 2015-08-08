package com.hstclair.jaskell.first;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author hstclair
 * @since 6/15/15 8:59 PM
 */
public class It<T extends Number, R> {



    Function<Integer, Integer> add(Integer i) {
        return (Integer it) -> it + i;
    }

    Function<Double, Double> add(Double i) {
        return (Double it) -> it + i;
    }

    BiFunction<Function<T,T>, Collection<T>, Collection<T>> map(Function<T, T> function, Collection<T> collection) {
        return () -> {
    }
}
