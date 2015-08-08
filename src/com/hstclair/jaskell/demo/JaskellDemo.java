package com.hstclair.jaskell.demo;

import com.hstclair.jaskell.data.LzList;

/**
 * @author hstclair
 * @since 7/9/15 9:06 PM
 */
public class JaskellDemo {

    private static Integer doubler(Integer intval) {
        return intval*2;
    }

    public static void demo() {
        LzList<Integer> positiveIntegers = LzList.iteration((value) -> value + 1, 0);

        LzList<Integer> highOrderPositiveIntegers = positiveIntegers.drop(100000).evaluate();

        LzList<Integer> subset = highOrderPositiveIntegers.take(10).evaluate();

        LzList<Integer> doubled = subset.map(indefinite -> indefinite.andThen(value -> value * 2) ).evaluate();

        LzList<Integer> smallerSubset = doubled.drop(3).evaluate();

        LzList<Integer> result = smallerSubset;

        for (int count = 0; count < result.length().evaluate(); count++) {
            System.out.printf("positive integer #%d is %d\n", count, result.get(count).evaluate());
        }
    }
}
