package com.hstclair.jaskell.function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/17/15 8:03 PM
 */
public class TestSubstitutionFunction {

    @Test
    public void testConstructFromFunction() {
        Function<Long, Long> expectedFunction = (it) -> it;
        Function<Long, Indefinite<Long>> expectedSubstitutionFunction = Indefinite::of;

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(expectedSubstitutionFunction, expectedFunction);

        assertEquals(expectedFunction, instance.originalFunction);
        assertEquals(expectedSubstitutionFunction, instance.substitutionFunction);
    }
}
