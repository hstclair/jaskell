package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import static junit.framework.Assert.*;

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

        assertEquals(lambda, instance.resultExpression);
    }

    @Test
    public void testConstructIndefiniteImplFromExpressionsSetsIsPresentExpression() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(isPresent, instance.isPresentExpression);
    }

    @Test
    public void testGetExpression() {
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(lambda, instance.getExpression());
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
        Boolean expected = true;
        Expression<Long> lambda = () -> 10l;
        Expression<Boolean> isPresent = () -> expected;

        IndefiniteImpl<Long> instance = new IndefiniteImpl<>(lambda, isPresent);

        assertEquals(expected, instance.isPresent());
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
        Boolean expected = true;
        Function<Long, Long> transformation = (it) -> it * 10;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> original = new IndefiniteImpl<Long>(()->input, isPresent);

        Indefinite<Long> instance = original.andThen(transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(expected, instance.isPresent());
        assertEquals(isPresent, instance.getIsPresentExpression());
    }

    @Test
    public void testAndThenConstructor() {
        Long input = 100l;
        Function<Long, Long> transformation = (it) -> it * 10;
        Long expected = transformation.apply(input);

        IndefiniteImpl<Long> original = new IndefiniteImpl<>(()->input, () -> true);

        Indefinite<Long> instance = new IndefiniteImpl<>(original, transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testAndThenConstructorRetainsIsPresentExpression() {
        Long input = 100l;
        Boolean expected = true;
        Function<Long, Long> transformation = (it) -> it * 10;
        Expression<Boolean> isPresent = () -> true;

        IndefiniteImpl<Long> original = new IndefiniteImpl<Long>(()->input, isPresent);

        Indefinite<Long> instance = new IndefiniteImpl<>(original, transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(expected, instance.isPresent());
        assertEquals(isPresent, instance.getIsPresentExpression());
    }

    @Test
    public void testAndThenConstructorForLambdaIndefinite() {
        Long input = 100l;
        Function<Long, Long> transformation = (it) -> it * 10;
        Long expected = transformation.apply(input);

        Indefinite<Long> original = ()->input;

        Indefinite<Long> instance = new IndefiniteImpl<>(original, transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testOrElseImplReturnsPrimaryWhenPresent() {
        Long expected = 100l;
        Long notExpected = -100l;
        Indefinite<Long> primary = () -> expected;
        Indefinite<Long> secondary = () -> notExpected;

        Indefinite<Long> instance = IndefiniteImpl.orElseImpl(primary, secondary);

        assertTrue(instance.isPresent());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testOrElseImplWithoutPrimaryReturnsSecondary() {
        Long expected = 100l;
        Indefinite<Long> primary = Indefinite.EMPTY;
        Indefinite<Long> secondary = () -> expected;

        Indefinite<Long> instance = IndefiniteImpl.orElseImpl(primary, secondary);

        assertTrue(instance.isPresent());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testGetTransformations() {
        Long input = 100l;
        Function<Long, Long> transformation = (it) -> it * 10;

        IndefiniteImpl<Long> original = new IndefiniteImpl<>(()->input, () -> true);

        Indefinite<Long> instance = original.andThen(transformation);

        assertEquals(IndefiniteImpl.class, instance.getClass());
        assertEquals(1, instance.getTransformations().size());
        assertEquals(transformation, instance.getTransformations().get(0));
    }
}
