package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author hstclair
 * @since 7/13/15 10:10 PM
 */
public class TestCachedExpression {

    @Test
    public void testCachedExpressionConstructorRejectsNullExpression() {
        try {
            new CachedExpression(null);
            fail();
        } catch (NullPointerException npe) {}
    }


    @Test
    public void testEvaluateReturnsTargetResult() {
        Long expected = 100l;
        Expression<Long> target = () -> expected;

        CachedExpression<Long> cachedExpression = new CachedExpression<>(target);

        assertEquals(expected, cachedExpression.evaluate());
    }

    @Test
    public void testEvaluateInvokesTargetOnlyOnce() {
        int[] invoked = {0};

        Long expected = 100l;

        Expression<Long> target = () -> { invoked[0]++; return expected; };

        Expression<Long> cached = new CachedExpression<>(target);

        assertEquals(expected, cached.evaluate());
        assertEquals(expected, cached.evaluate());
        assertEquals(expected, cached.evaluate());

        assertEquals(invoked[0], 1);
    }
}
