package com.hstclair.jaskell.function;

import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * @author hstclair
 * @since 7/13/15 9:36 PM
 */
public class TestIndefiniteImpl {

    @Test
    public void testEmpty() {
        assertFalse(IndefiniteImpl.EMPTY.isPresent());

        try {
            IndefiniteImpl.EMPTY.evaluate();
        } catch (IllegalStateException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructIndefiniteImplFromExpressionsSetsExpression() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(lambda, instance.evaluateExpression);
    }

    @Test
    public void testConstructIndefiniteImplFromExpressionsSetsIsPresentExpression() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(isPresent, instance.isPresentExpression);
    }

    @Test
    public void testGetIsPresentExpression() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(isPresent, instance.getIsPresentExpression());
    }

    @Test
    public void testEvaluate() {
        Long expected = 10l;
        Expression<Long> lambda = () -> expected;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testIsPresent() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(Boolean.TRUE, instance.isPresent());
    }

    @Test
    public void testAndThenAppliesTransformFunction() {
        Long input = 100l;
        Function<Long, Long> transformation = (it) -> it * 10;
        Long expected = transformation.apply(input);

        IndefiniteImpl<Long> original = new IndefiniteImpl<>(()->input, () -> true);

        Indefinite<Long> instance = original.andThen(transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testAndThenRetainsIsPresentExpression() {
        Long input = 100l;
        Function<Long, Long> transformation = (it) -> it * 10;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> original = new IndefiniteImpl<>(() -> input, isPresent);

        Indefinite<Long> instance = original.andThen(transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(Boolean.TRUE, instance.isPresent());
        assertEquals(isPresent, instance.getIsPresentExpression());
    }
}
