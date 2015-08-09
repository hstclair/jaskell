package com.hstclair.jaskell.function;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/11/15 11:42 PM
 */
public class TestExpression {

    void pass() {}

    @Test
    public void testAlwaysTrue() {
        assertTrue(Expression.alwaysTrue.evaluate());
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Expression.alwaysFalse.evaluate());
    }

    @Test
    public void testAlwaysOne() {
        assertEquals(1l, (long) Expression.alwaysOne.evaluate());
    }

    @Test
    public void testAlwaysZero() {
        assertEquals(0l, (long) Expression.alwaysZero.evaluate());
    }

    @Test
    public void testAlwaysNegativeOne() {
        assertEquals(-1l, (long) Expression.alwaysNegativeOne.evaluate());
    }

    @Test
    public void testAlwaysNull() {
        assertNull(Expression.alwaysNull.evaluate());
    }

    @Test
    public void testExpressionOfRejectsNull() {
        try {
            Expression.of(null);
        } catch (NullPointerException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testExpressionOfLambdaIsLambda() {
        Long expected = 100l;
        boolean[] invoked = { false };

        Expression<Long> lambda = () -> { invoked[0] = true; return expected; };
        Expression expression = Expression.of(lambda);

        assertEquals(lambda, expression);
        assertEquals(expected, expression.evaluate());
        assertTrue(invoked[0]);
    }

    @Test
    public void testFromRejectsNullSupplier() {
        try {
            Expression.from(null);
            fail();
        } catch (NullPointerException npe) {pass();}
    }

    @Test
    public void testInvokingExpressionFromSupplierInvokesSupplier() {
        Long expected = 100l;
        boolean[] invoked = { false };

        java.util.function.Supplier<Long> supplier = () -> { invoked[0] = true; return expected; };

        Expression<Long> instance = Expression.from(supplier);

        assertNotEquals(instance, supplier);
        assertEquals(expected, instance.evaluate());
        assertTrue(invoked[0]);
    }

    @Test
    public void testInvokingExpressionAsSupplierInvokesExpression() {
        Long expected = 100l;
        boolean[] invoked = { false };

        Expression<Long> expression = () -> { invoked[0] = true; return expected; };

        java.util.function.Supplier<Long> instance = expression.asSupplier();

        assertNotEquals(instance, expression);
        assertEquals(expected, instance.get());
        assertTrue(invoked[0]);
    }

    @Test
    public void testExpressionAndThenRejectsNullFunction() {
        Expression<Object> instance = Object::new;

        try {
            instance.andThen(null);
            fail();
        } catch (NullPointerException npe) {pass();}
    }

    @Test
    public void testExpressionAndThenInvokesExpression() {
        boolean[] invoked = { false };

        Expression<Long> expression = () -> { invoked[0] = true; return 0l; };

        Expression<Long> finalEval = expression.andThen(Function.identity());

        finalEval.evaluate();

        assertTrue(invoked[0]);
    }

    @Test
    public void testExpressionAndThenPassesExpressionResultToFunction() {
        boolean[] invoked = { false };

        Long expected = 100l;

        Expression<Long> expression = () -> expected;

        Expression<Long> finalEval = expression.andThen((it) -> { invoked[0] = true; assertEquals(expected, it); return it; });

        finalEval.evaluate();

        assertTrue(invoked[0]);
    }

    @Test
    public void testExpressionAndThenReturnsFunctionResult() {
        Long expected = 100l;
        Long notExpected = -100l;

        Expression<Long> expression = () -> notExpected;

        Expression<Long> finalEval = expression.andThen((it) -> { assertEquals(notExpected, it); return expected; });

        Long result = finalEval.evaluate();

        assertEquals(expected, result);
    }

    @Test
    public void testCacheReturnsCachedExpressionThatInvokesExpression() {
        boolean[] invoked = {false};
        Long expected = 100l;
        Expression<Long> lambda = () -> { invoked[0] = true; return expected; };

        Expression<Long> instance = lambda.cache();

        assertEquals(instance.getClass(), CachedExpression.class);
        assertEquals(expected, instance.evaluate());
        assertTrue(invoked[0]);
    }

    @Test
    public void testInvokingUnwrappedExpressionInvokesWrappedExpression() {
        Object[] expected = {null};
        long innerInvoked[] = {0};
        long outerInvoked[] = {0};
        Expression<Object> inner = () -> {
            innerInvoked[0]++;
            return expected[0] = new Object();
        };

        Expression<Expression<Object>> outer = () -> { outerInvoked[0]++; return inner; };
        Expression<Object> instance = Expression.unwrap(outer);

        assertEquals(0, innerInvoked[0]);
        assertEquals(0, outerInvoked[0]);

        Object result = instance.evaluate();

        assertEquals(1, innerInvoked[0]);
        assertEquals(1, outerInvoked[0]);

        assertEquals(expected[0], result);
    }

//    @Test
//    public void testSubstituteReturnsSubstituteValueWhenSubstituteValueIsPresent() {
//        Long notExpected = -100l;
//        Long expected = 100l;
//        boolean[] notInvoked = {true};
//        Expression<Long> lambda = () -> { notInvoked[0] = false; return notExpected; };
//
//        Expression<Long> instance = lambda.substitute(Indefinite.of(expected));
//
//        assertEquals(expected, instance.evaluate());
//        assertTrue(notInvoked[0]);
//    }
//
//    @Test
//    public void testSubstituteReturnsOriginalValueWhenSubstituteValueIsNotPresent() {
//        Long expected = 100l;
//        boolean[] invoked = {false};
//        Expression<Long> lambda = () -> { invoked[0] = true; return expected; };
//
//        Expression<Long> instance = lambda.substitute(Indefinite.EMPTY);
//
//        assertEquals(expected, instance.evaluate());
//        assertTrue(invoked[0]);
//    }
//
//
//    private static <T> Expression<T> wrap(T t) {
//        return Expression.of(() -> t);
//    }
//
//    @Test
//    public void testUnwrap() {
//        Long expected = 100l;
//
//        Expression<Long> supplier = () -> expected;
//
//        Expression<Expression<Long>> wrapped = wrap(supplier);
//
//        Expression<Long> unwrapped = Expression.unwrap(wrapped);
//
//        assertNotEquals(unwrapped, supplier);
//        assertEquals(expected, unwrapped.evaluate());
//    }
//
//    @Test
//    public void testSubstitutePresent() {
//        Long expected = 100l;
//        Long notExpected = -expected;
//
//        Expression<Long> expression = Expression.of(() -> notExpected);
//
//        Expression<Long> substitutedExpression = expression.substitute(Indefinite.of(expected));
//
//        assertEquals(expected, substitutedExpression.evaluate());
//    }
//
//    @Test
//    public void testSubstituteNotPresent() {
//        Long expected = 100l;
//        Long notExpected = -expected;
//
//        Expression<Long> supplier = Expression.of(() -> expected);
//
//        Expression<Long> substitutedSupplier = supplier.substitute(Indefinite.of(() -> notExpected, () -> false));
//
//        assertEquals(expected, substitutedSupplier.evaluate());
//    }
//
//    @Test
//    public void testGetExpression() {
//        Expression<Long> expected = () -> 1l;
//
//        Expression<Long> result = expected.getExpression();
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testGetTransformations() {
//        Expression<Long> lambda = () -> 1l;
//
//        List<Function> expected = Collections.EMPTY_LIST;
//
//        List<Function> result = lambda.getTransformations();
//
//        assertEquals(expected, result);
//    }
}
