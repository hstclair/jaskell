package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Expression;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/26/15 2:41 PM
 */
public class TestFunctionalIteratorImpl {

    @Test
    public void testConstructorFromExpressions() {
        Expression<Object> expectedNext = Object::new;
        Expression<Boolean> expectedHasNext = () -> true;

        FunctionalIteratorImpl<Object> instance = new FunctionalIteratorImpl<>(expectedNext, expectedHasNext);

        assertEquals(expectedNext, instance.nextExpression);
        assertEquals(expectedHasNext, instance.hasNextExpression);
    }

    @Test
    public void testConstructorFromExpressionsRejectsNullNext() {

        try {
            new FunctionalIteratorImpl<>(null, Expression.alwaysTrue);
            fail();
        } catch (NullPointerException ex) {}

    }

    @Test
    public void testConstructorFromExpressionsRejectsNullHasNext() {

        try {
            new FunctionalIteratorImpl<>(Object::new, null);
            fail();
        } catch (NullPointerException ex) {}

    }

    @Test
    public void testConstructorFromStrategy() {
        Expression<Object> expectedNext = Object::new;
        Expression<Boolean> expectedHasNext = () -> true;

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(expectedNext, expectedHasNext);

        FunctionalIteratorImpl<Object> instance = new FunctionalIteratorImpl<>(strategy);

        assertEquals(expectedNext, instance.nextExpression);
        assertEquals(expectedHasNext, instance.hasNextExpression);
    }

    @Test
    public void testConstructorFromStrategyRejectsNullStrategy() {

        try {
            new FunctionalIteratorImpl<>(null);
            fail();
        } catch (NullPointerException ex) {}

    }

    @Test
    public void testHasNextInvokesExpression() {

        final Boolean[] invoked = new Boolean[] {false};

        Expression<Boolean> hasNextExpression = () -> { invoked[0] = true; return true; };

        FunctionalIteratorImpl<Object> instance = new FunctionalIteratorImpl<>(Object::new, hasNextExpression);

        instance.hasNext();

        assertTrue(invoked[0]);
    }

    @Test
    public void testNextInvokesExpression() {

        final Boolean[] invoked = new Boolean[] {false};

        Expression<Object> nextExpression = () -> { invoked[0] = true; return new Object(); };

        FunctionalIteratorImpl<Object> instance = new FunctionalIteratorImpl<Object>(nextExpression, Expression.alwaysTrue);

        instance.next();

        assertTrue(invoked[0]);
    }

    @Test
    public void testNextReturnsExpressionResult() {

        final Object[] expected = new Object[] { null };

        Expression<Object> nextExpression = () -> expected[0] = new Object();

        FunctionalIteratorImpl<Object> instance = new FunctionalIteratorImpl<>(nextExpression, Expression.alwaysTrue);

        Object result = instance.next();

        assertEquals(expected[0], result);
    }
}
