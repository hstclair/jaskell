package com.hstclair.jaskell.function;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/11/15 10:04 PM
 */
public class TestIndefinite {
    @Test
    public void testIndefiniteOfValue() {
        Long expected = 100l;

        Indefinite<Long> osl = Indefinite.of(expected);

        assertTrue(osl.isPresent());
        assertEquals(expected, osl.evaluate());
    }

    @Test
    public void testIndefiniteOfExpression() {
        Long expected = 100l;

        Expression<Long> lambda = () -> expected;

        Indefinite<Long> osl = Indefinite.of(lambda);

        assertTrue(osl.isPresent());
        assertEquals(expected, osl.evaluate());
    }

    @Test
    public void testIndefiniteOfExpressionImpl() {
        Long expected = 100l;

        Expression<Long> expression = new ExpressionImpl<Long>(() -> expected);

        Indefinite<Long> osl = Indefinite.of(expression);

        assertTrue(osl.isPresent());
        assertEquals(expected, osl.evaluate());
    }

    @Test
    public void testIndefiniteOfNull() {
        Object nothing = null;

        try {
            Indefinite<Object> osl = Indefinite.of(nothing);
        } catch (NullPointerException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testIndefiniteOfExpressions() {
        Long expected = 100l;

        Indefinite<Long> osl = Indefinite.of(() -> expected, () -> true);

        assertTrue(osl.isPresent());
        assertEquals(expected, osl.evaluate());
    }

    @Test
    public void testIndefiniteAndThen() {
        Long a = 10l;
        Long b = 20l;
        String expected = Long.toString(a * b);

        Indefinite<Long> original = Indefinite.of(a);
        Indefinite<String> modified = original.andThen(x -> Long.toString(x * b));

        assertTrue(modified.isPresent());
        assertEquals(expected, modified.evaluate());
    }

    @Test
    public void testIndefiniteExpressionOrElse() {
        Long expected = 100l;
        Long notExpected = 60l;

        Indefinite<Long> indefinite = () -> expected;

        Expression<Long> orElse = indefinite.orElse(() -> notExpected);

        assertEquals(expected, orElse.evaluate());

    }

    @Test
    public void testIndefiniteIsPresentExpression() {
        Long expected = 100l;

        Indefinite<Long> indefinite = () -> expected;

        Expression<Boolean> isPresent =  indefinite.getIsPresentExpression();

        assertTrue(isPresent.evaluate());
    }

    @Test
    public void testExtractIsPresent() {
        Indefinite<Long> inner = Indefinite.EMPTY;

        Indefinite<Indefinite<Long>> target = Indefinite.of(inner);

        Boolean isPresent = Indefinite.extractIsPresent(target);

        assertFalse(isPresent);
    }

    @Test
    public void testUnwrapIndefiniteIndefiniteEmpty() {
        Indefinite<Indefinite<Long>> target = Indefinite.of(Indefinite.EMPTY);

        try {
            Long expression = Indefinite.<Long> extractExpression(target, () -> Indefinite.extractIsPresent(target));

        } catch (IllegalStateException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testEmptyIndefiniteOrElse() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.EMPTY.orElse(expected).evaluate());
    }

    @Test
    public void testNonEmptyIndefiniteOrElse() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.of(expected).orElse(notExpected).evaluate());
    }

    @Test
    public void testEmptyIndefiniteOrElseExpression() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.EMPTY.orElse(() -> expected).evaluate());
    }

    @Test
    public void testNonEmptyIndefiniteOrElseExpression() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.of(expected).orElse(() -> notExpected).evaluate());
    }

    @Test
    public void testEmptyIndefiniteOrElseNonEmptyIndefinite() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.EMPTY.orElse(Indefinite.of(expected)).evaluate());
    }

    @Test
    public void testNonEmptyIndefiniteOrElseIndefinite() {
        Long expected = 100l;
        Long notExpected = -100l;

        assertEquals(expected, Indefinite.of(expected).orElse(() -> notExpected).evaluate());
    }

    @Test
    public void testEmptyIndefiniteOrElseEmptyIndefinite() {
        assertFalse(Indefinite.EMPTY.orElse(Indefinite.EMPTY).isPresent());
    }

    class IncrementingIndefinite implements Expression<Long> {
        long value;

        boolean present = false;

        public Long evaluate() { return value++; }

        public Boolean isPresent() {
            return present = ! present;
        }
    }

    @Test
    public void testMutableIndefinite() {
        Indefinite<Long> indefinite = Indefinite.of(new IncrementingIndefinite());

        long a = indefinite.evaluate();
        long b = indefinite.evaluate();

        assertNotEquals(a, b);
    }

    @Test
    public void testCachedMutableIndefinite() {
        Indefinite<Long> indefinite = Indefinite.of(new IncrementingIndefinite()).cache();

        long a = indefinite.evaluate();
        long b = indefinite.evaluate();

        assertEquals(a, b);
    }

    @Test
    public void testMutableIndefiniteIsPresent() {
        IncrementingIndefinite incrementingSupplier = new IncrementingIndefinite();

        Indefinite<Long> indefinite = Indefinite.of(incrementingSupplier::evaluate, incrementingSupplier::isPresent);

        boolean a = indefinite.isPresent();
        boolean b = indefinite.isPresent();

        assertNotEquals(a, b);
    }

    @Test
    public void testCachedMutableOptionalSupplierIsPresent() {
        IncrementingIndefinite incrementingSupplier = new IncrementingIndefinite();

        Indefinite<Long> indefinite = Indefinite.of(incrementingSupplier::evaluate, incrementingSupplier::isPresent).cache();

        boolean a = indefinite.isPresent();
        boolean b = indefinite.isPresent();

        assertEquals(a, b);
    }

    private static <T> Indefinite<T> wrap(T t) {
        return Indefinite.of(t);
    }

    @Test
    public void testUnwrapIndefinite() {
        Long expected = 100l;

        Indefinite<Long> indefinite = Indefinite.of(expected);

        Indefinite<Indefinite<Long>> optionalSupplierIndefinite = wrap(indefinite);

        Indefinite<Long> unwrapped = Indefinite.unwrap(optionalSupplierIndefinite);

        assertTrue(unwrapped.isPresent());
        assertEquals(expected, unwrapped.evaluate());
    }

    @Test
    public void testUnwrapEmptyIndefinite() {
        Indefinite<Indefinite<Long>> wrapped = wrap(Indefinite.EMPTY);

        Indefinite<Long> unwrapped = Indefinite.unwrap(wrapped);

        assertFalse(unwrapped.isPresent());
    }

    @Test
    public void testUnwrapExpressionOfIndefinite() {
        Long expected = 100l;

        Expression<Indefinite<Long>> supplier = () -> Indefinite.of(expected);

        Indefinite<Long> unwrapped = Indefinite.unwrap(supplier);

        assertTrue(unwrapped.isPresent());
        assertEquals(expected, unwrapped.evaluate());
    }

    @Test
    public void testOptionalSupplierEmpty() {
        assertFalse(Indefinite.EMPTY.isPresent());
    }

    @Test
    public void testIndefiniteFunctionalPresent() {
        Expression<Boolean> isPresent = () -> true;
        Expression<Object> expression = () -> null;

        Indefinite<Object> indefinite = Indefinite.of(expression, isPresent);

        assertTrue(indefinite.isPresent());
    }

    @Test
    public void testIndefiniteFunctionalNotPresent() {
        Expression<Boolean> isPresent = () -> false;
        Expression<Object> expression = () -> null;

        Indefinite<Object> indefinite = Indefinite.of(expression, isPresent);

        assertFalse(indefinite.isPresent());
    }

    @Test
    public void testIndefiniteFunctionalGetPresent() {
        Object expected = new Object();

        Expression<Boolean> isPresent = () -> true;
        Expression<Object> expression = () -> expected;

        Indefinite<Object> indefinite = Indefinite.of(expression, isPresent);

        assertTrue(indefinite.isPresent());
        assertEquals(expected, indefinite.evaluate());
    }

    @Test
    public void testIndefiniteFunctionalGetNotPresent() {
        Object expected = new Object();

        Expression<Boolean> isPresent = () -> false;
        Expression<Object> expression = () -> expected;

        Indefinite<Object> indefinite = Indefinite.of(expression, isPresent);

        assertFalse(indefinite.isPresent());
        assertEquals(expected, indefinite.evaluate());
    }
}
