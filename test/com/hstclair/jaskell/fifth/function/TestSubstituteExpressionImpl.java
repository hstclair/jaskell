package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/16/15 7:08 PM
 */
public class TestSubstituteExpressionImpl {

    @Test
    public void testConstructor() {
        Expression<Long> expectedExpression = () -> 22l;
        Indefinite<Long> expectedIndefinte = () -> 33l;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        assertEquals(expectedExpression, instance.expression);
        assertEquals(expectedIndefinte, instance.substituteSupplier);
    }

    @Test
    public void testSubstituteReturnsIndefiniteWhenPresent() {
        Long expected = 33l;

        Expression<Long> expectedExpression = () -> -expected;
        Indefinite<Long> expectedIndefinte = () -> expected;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testSubstituteReturnsExpressionWhenIndefiniteNotPresent() {
        Long expected = 33l;

        Expression<Long> expectedExpression = () -> expected;
        Indefinite<Long> expectedIndefinte = Indefinite.EMPTY;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testAndThenReturnsExpressionImpl() {
        Long input = 33l;

        Expression<Long> expectedExpression = () -> input;
        Indefinite<Long> expectedIndefinte = Indefinite.EMPTY;

        Function<Long, Long> transform = (it) -> it * 2;

        SubstituteExpressionImpl<Long> original = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        Expression<Long> instance = original.andThen(transform);

        assertEquals(ExpressionImpl.class, instance.getClass());
    }

    @Test
    public void testExpressionAndThen() {
        Long input = 33l;

        Expression<Long> expectedExpression = () -> input;
        Indefinite<Long> expectedIndefinte = Indefinite.EMPTY;

        Function<Long, Long> transform = (it) -> it * 2;

        SubstituteExpressionImpl<Long> original = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        Expression<Long> instance = original.andThen(transform);
        Long expected = transform.apply(input);

        assertEquals(ExpressionImpl.class, instance.getClass());
        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testIndefiniteAndThen() {
        Long input = 33l;

        Expression<Long> expectedExpression = () -> -input;
        Indefinite<Long> expectedIndefinte = () -> input;

        Function<Long, Long> transform = (it) -> it * 2;

        SubstituteExpressionImpl<Long> original = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinte);

        Expression<Long> instance = original.andThen(transform);
        Long expected = transform.apply(input);

        assertEquals(expected, instance.evaluate());
    }

    @Test
    public void testGetExpressionReturnsExpressionWhenIndefiniteEmpty() {
        Long expected = 33l;

        Expression<Long> expectedExpression = () -> expected;
        Indefinite<Long> expectedIndefinite = Indefinite.EMPTY;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinite);

        assertEquals(expected, instance.getExpression().evaluate());
    }

    @Test
    public void testGetExpressionReturnsIndefiniteWhenIndefinitePresent() {

        Long expected = 100l;

        Expression<Long> expression = () -> expected;

        Indefinite<Long> expectedIndefinite = Indefinite.of(expression);

        Expression<Long> expectedExpression = () -> -expected;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinite);

        assertEquals(expected, instance.getExpression().evaluate());
    }

    @Test
    public void testGetTransformationsWhenIndefiniteNotPresent() {
        Long input = 100l;

        Function<Long, Long> transformation = (it) -> it * 3;

        Expression<Long> expression = () -> input;

        Expression<Long> expectedExpression = expression.andThen(transformation);

        Indefinite<Long> expectedIndefinite = Indefinite.EMPTY;

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expectedExpression, expectedIndefinite);

        Long expected = transformation.apply(input);

        assertEquals(expected, instance.evaluate());
        assertEquals(1, instance.getTransformations().size());
        assertEquals(transformation, instance.getTransformations().get(0));
    }

    @Test
    public void testGetTransformationsWhenIndefinitePresent() {
        Long input = 100l;

        Function<Long, Long> transformation = (it) -> it * 3;

        Expression<Long> expression = () -> input;

        Expression<Long> expression2 = expression.andThen(transformation);

        Indefinite<Long> expectedIndefinite = Indefinite.of(expression);

        SubstituteExpressionImpl<Long> instance = new SubstituteExpressionImpl<Long>(expression2, expectedIndefinite);

        Long expected = expression.evaluate();

        assertEquals(expected, instance.evaluate());
        assertEquals(0, instance.getTransformations().size());
    }
}
