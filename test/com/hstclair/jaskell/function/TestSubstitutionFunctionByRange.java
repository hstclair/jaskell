package com.hstclair.jaskell.function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/17/15 9:59 PM
 */
public class TestSubstitutionFunctionByRange {

    @Test
    public void testConstructFromFunction() {
        Function<Long, Boolean> rangeFunction = (it) -> true;
        Function<Long, Long> substituteFunction = (it) -> it;
        Function<Long, Long> originalFunction = (it) -> it;

        SubstitutionFunctionByRange<Long, Long> instance = new SubstitutionFunctionByRange<>(rangeFunction, substituteFunction, originalFunction);

        assertEquals(rangeFunction, instance.rangeFunction);
        assertEquals(substituteFunction, instance.substituteFunction);
        assertEquals(originalFunction, instance.originalFunction);
    }

}
