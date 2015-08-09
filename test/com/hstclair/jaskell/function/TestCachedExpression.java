package com.hstclair.jaskell.function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author hstclair
 * @since 7/13/15 10:10 PM
 */
public class TestCachedExpression {

    void pass() {}

    @Test
    public void testCachedExpressionConstructorRejectsNullExpression() {
        try {
            new CachedExpression<>(null);
            fail();
        } catch (NullPointerException npe) {pass();}
    }


    @Test
    public void testEvaluateReturnsExpressionResult() {
        Object[] expected = {null};
        Expression<Object> target = () -> expected[0] = new Object();

        CachedExpression<Object> cachedExpression = new CachedExpression<>(target);

        Object result = cachedExpression.evaluate();

        assertEquals(expected[0], result);
    }

    @Test
    public void testEvaluateInvokesTargetOnlyOnce() {
        int[] invoked = {0};

        Expression<Object> target = () -> { invoked[0]++; return new Object(); };

        Expression<Object> cached = new CachedExpression<>(target);

        cached.evaluate();
        cached.evaluate();

        assertEquals(invoked[0], 1);
    }
}
