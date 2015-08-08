package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Function;
import com.hstclair.jaskell.fifth.function.Indefinite;
import com.sun.org.apache.bcel.internal.generic.FADD;
import org.junit.Test;

import java.util.Objects;
import java.util.function.BiFunction;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/26/15 2:42 PM
 */
public class TestFunctionalIteratorUntilStrategy {

    class Mock extends FunctionalIteratorUntilStrategy<Object> {

        BiFunction<Expression<Boolean>, Expression<Object>, Indefinite<Object>> getCurrentOrNextFunction;

        Function<Indefinite<Object>, Object> consumeCurrentFunction;

        public Mock(Expression<Object> nextExpression, Expression<Boolean> hasNextExpression, Function<Object, Boolean> untilPredicate) {
            super(nextExpression, hasNextExpression, untilPredicate);

            getCurrentOrNextFunction = super::getCurrentOrNext;
            consumeCurrentFunction = super::consumeCurrent;
        }

        @Override
        Indefinite<Object> getCurrentOrNext(Expression<Boolean> originalHasNextExpression, Expression<Object> originalNextExpression) {
            return getCurrentOrNextFunction.apply(originalHasNextExpression, originalNextExpression);
        }

        @Override
        Object consumeCurrent(Indefinite<Object> currentIndefinite) {
            return consumeCurrentFunction.apply(currentIndefinite);
        }

    }

    private FunctionalIteratorUntilStrategy<Object> buildInstance(Function<Object, Boolean> predicate) {
        return new Mock(Object::new, Expression.alwaysTrue, predicate);
    }

    private FunctionalIteratorUntilStrategy<Object> buildInstance() {
        return buildInstance((it) -> false);
    }

    @Test
    public void testConstructorFromStrategyAndPredicate() {
        Expression<Object> expectedNext = Object::new;
        Expression<Boolean> expectedHasNext = () -> true;
        Function<Object, Boolean> expectedPredicate = (it) -> true;

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(expectedNext, expectedHasNext);

        FunctionalIteratorUntilStrategy<Object> instance = new FunctionalIteratorUntilStrategy<>(strategy, expectedPredicate);

        assertEquals(expectedPredicate, instance.untilPredicate);
    }

    @Test
    public void testGetCurrentOrNextReturnsCurrentWhenPresent() {
        Indefinite<Object> expected = Indefinite.of(new Object());

        FunctionalIteratorUntilStrategy<Object> instance = buildInstance();

        instance.current = expected;

        Indefinite<Object> result = instance.getCurrentOrNext(() -> true, Object::new);

        assertTrue(result.isPresent());
        assertEquals(expected, result);
        assertEquals(result, instance.current);
    }

    @Test
    public void testGetCurrentOrNextReturnsEmptyWhenPredicateSatisfied() {
        Expression<Boolean> neverInvokedHasNext = () -> { fail(); return false; };
        Expression<Object> neverInvokedNext = () -> { fail(); return new Object(); };
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance();

        instance.current = Indefinite.EMPTY;
        instance.predicateSatisfied = true;

        Indefinite<Object> result = instance.getCurrentOrNext(neverInvokedHasNext, neverInvokedNext);

        assertEquals(Indefinite.EMPTY, result);
        assertEquals(result, instance.current);
    }

    @Test
    public void testGetCurrentOrNextReturnsEmptyWhenOriginalHasNextReturnsFalse() {
        Boolean[] invoked = new Boolean[] {false};

        Expression<Boolean> originalHasNext = () -> { invoked[0] = true; return false; };
        Expression<Object> neverInvokedNext = () -> { fail(); return new Object(); };
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance();

        instance.current = Indefinite.EMPTY;

        Indefinite<Object> result = instance.getCurrentOrNext(originalHasNext, neverInvokedNext);

        assertTrue(invoked[0]);
        assertEquals(Indefinite.EMPTY, result);
        assertEquals(result, instance.current);
    }

    @Test
    public void testGetCurrentOrNextReturnsNextWhenOriginalHasNextReturnsTrueAndPredicateUnsatisfied() {
        Boolean[] invoked = new Boolean[] {false};
        Object[] expected = new Object[] {null};
        Boolean[] predicateInvoked = new Boolean[] {false};

        Function<Object, Boolean> predicate = (it) -> ! (predicateInvoked[0] = true);

        Expression<Boolean> originalHasNext = Expression.alwaysTrue;
        Expression<Object> originalNext = () -> { invoked[0] = true; return expected[0] = new Object(); };
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance(predicate);

        instance.current = Indefinite.EMPTY;

        Indefinite<Object> result = instance.getCurrentOrNext(originalHasNext, originalNext);

        assertTrue(invoked[0]);
        assertTrue(predicateInvoked[0]);
        assertTrue(result.isPresent());
        assertEquals(expected[0], result.evaluate());
        assertEquals(result, instance.current);
    }

    @Test
    public void testGetCurrentOrNextReturnsEmptyWhenNextSatisfiesPredicate() {
        Boolean[] invoked = new Boolean[] {false};

        Function<Object, Boolean> predicate = (it) -> invoked[0] = true;

        Expression<Boolean> originalHasNext = Expression.alwaysTrue;
        Expression<Object> originalNext = Object::new;
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance(predicate);

        instance.current = Indefinite.EMPTY;

        Indefinite<Object> result = instance.getCurrentOrNext(originalHasNext, originalNext);

        assertTrue(invoked[0]);
        assertEquals(Indefinite.EMPTY, result);
        assertEquals(result, instance.current);
    }

    @Test
    public void testConsumeCurrentReturnsEvaluatedCurrent() {
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance();

        Object expected = new Object();

        Indefinite<Object> currentIndefinite = Indefinite.of(expected);

        Object result = instance.consumeCurrent(currentIndefinite);

        assertEquals(result, expected);
    }

    @Test
    public void testConsumeCurrentThrowsExceptionWhenCurrentEmpty() {
        FunctionalIteratorUntilStrategy<Object> instance = buildInstance();

        try {
            instance.consumeCurrent(Indefinite.EMPTY);
            fail();
        } catch (IllegalStateException ex) {}
    }

    @Test
    public void testHasNextInvokesCurrentOrNext() {
        Boolean[] invoked = new Boolean[] {false};

        Mock instance = (Mock) buildInstance();

        BiFunction<Expression<Boolean>, Expression<Object>, Indefinite<Object>> originalCurrentOrNext = instance.getCurrentOrNextFunction;

        instance.getCurrentOrNextFunction = (boolexp, objexp) -> { invoked[0] = true; return originalCurrentOrNext.apply(boolexp, objexp); };

        instance.hasNextExpression.evaluate();

        assertTrue(invoked[0]);
    }

    @Test
    public void testHasNextReturnsFalseWhenCurrentOrNextReturnsEmpty() {
        Mock instance = (Mock) buildInstance();

        instance.getCurrentOrNextFunction = (boolexp, objexp) -> Indefinite.EMPTY;

        assertFalse(instance.hasNextExpression.evaluate());
    }

    @Test
    public void testHasNextReturnsTrueWhenCurrentOrNextReturnsNonEmpty() {
        Mock instance = (Mock) buildInstance();

        instance.getCurrentOrNextFunction = (boolexp, objexp) -> Indefinite.of(1);

        assertTrue(instance.hasNextExpression.evaluate());
    }

    @Test
    public void testNextInvokesCurrentOrNext() {
        Boolean[] invoked = new Boolean[] {false};

        Mock instance = (Mock) buildInstance();

        BiFunction<Expression<Boolean>, Expression<Object>, Indefinite<Object>> originalCurrentOrNext = instance.getCurrentOrNextFunction;

        instance.getCurrentOrNextFunction = (boolexp, objexp) -> { invoked[0] = true; return originalCurrentOrNext.apply(boolexp, objexp); };

        instance.nextExpression.evaluate();

        assertTrue(invoked[0]);
    }

    @Test
    public void testNextInvokesConsumeCurrent() {
        Boolean[] invoked = new Boolean[] {false};

        Mock instance = (Mock) buildInstance();

        Function<Indefinite<Object>, Object> consumeCurrentFunction = instance.consumeCurrentFunction;

        instance.consumeCurrentFunction = (indefinite) -> { invoked[0] = true; return consumeCurrentFunction.apply(indefinite); };

        instance.nextExpression.evaluate();

        assertTrue(invoked[0]);
    }

    @Test
    public void testNextReturnsResultOfConsumeCurrent() {
        Boolean[] invoked = new Boolean[] {false};
        Object[] expected = new Object[] {null};

        Mock instance = (Mock) buildInstance();

        Function<Indefinite<Object>, Object> consumeCurrentFunction = instance.consumeCurrentFunction;

        instance.consumeCurrentFunction = (indefinite) -> { invoked[0] = true; return expected[0] = new Object(); };

        Object result = instance.nextExpression.evaluate();

        assertTrue(invoked[0]);
        assertEquals(expected[0], result);
    }
}
