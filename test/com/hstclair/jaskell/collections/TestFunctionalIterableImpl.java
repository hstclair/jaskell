package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Expression;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/26/15 2:39 PM
 */
public class TestFunctionalIterableImpl {

    private FunctionalIteratorStrategy<Object> buildStrategy() {
        return new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);
    }

    private <T> FunctionalIteratorStrategy<T> buildStrategy(T objResult) {
        return new FunctionalIteratorStrategy<>(() -> objResult, Expression.alwaysTrue);
    }

    private FunctionalIteratorStrategy<Object> buildDepletedStrategy() {
        return new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysFalse);
    }

    private <T> FunctionalIteratorStrategy<T> buildStrategy(Expression<T> nextExpression, Expression<Boolean> hasNextExpression) {

        return new FunctionalIteratorStrategy<>(nextExpression, hasNextExpression);
    }

    @Test
    public void testConstructor() {

        FunctionalIteratorStrategy<Object> strategy = buildStrategy();

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> instance = new FunctionalIterableImpl<>(strategyExpression);

        assertEquals(strategyExpression, instance.strategyExpression);
    }

    @Test
    public void testConstructorRejectsNullStrategy() {

        try {
            new FunctionalIterableImpl<>(null);
            fail();
        } catch (Exception ex) {}
    }

    @Test
    public void testIteratorInvokesStrategyExpression() {
        FunctionalIteratorStrategy<Object> strategy = buildStrategy();

        Boolean[] invoked = new Boolean[] { false };

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> { invoked[0] = true; return strategy; };

        FunctionalIterableImpl<Object> instance = new FunctionalIterableImpl<>(strategyExpression);

        instance.iterator();

        assertTrue(invoked[0]);
    }

    @Test
    public void testSpliteratorInvokesStrategyExpression() {
        FunctionalIteratorStrategy<Object>  strategy = buildStrategy();

        Boolean[] invoked = new Boolean[] { false };

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> { invoked[0] = true; return strategy; };

        FunctionalIterableImpl<Object> instance = new FunctionalIterableImpl<>(strategyExpression);

        instance.spliterator();

        assertTrue(invoked[0]);
    }

    @Test
    public void testIteratorUsesStrategy() {
        FunctionalIteratorStrategy<Object>  strategy = buildStrategy();

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> instance = new FunctionalIterableImpl<>(strategyExpression);

        FunctionalIterator<Object> iterator = instance.iterator();

        assertEquals(FunctionalIteratorImpl.class, iterator.getClass());

        FunctionalIteratorImpl<Object> iteratorInst = (FunctionalIteratorImpl<Object>) iterator;

        assertEquals(strategy.nextExpression, iteratorInst.nextExpression);

        assertEquals(strategy.hasNextExpression, iteratorInst.hasNextExpression);
    }

    @Test
    public void testSpliteratorUsesStrategy() {
        FunctionalIteratorStrategy<Object> strategy = buildStrategy();

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> instance = new FunctionalIterableImpl<>(strategyExpression);

        FunctionalSpliterator<Object> spliterator = instance.spliterator();

        assertEquals(FunctionalSpliteratorImpl.class, spliterator.getClass());

        FunctionalSpliteratorImpl<Object> concreteSpliterator = (FunctionalSpliteratorImpl<Object>) spliterator;

        assertEquals(strategy, concreteSpliterator.strategy);
    }

    @Test
    public void testTakeUntilReturnsFunctionalIterableImpl() {
        Object expectedObject = new Object();

        FunctionalIteratorStrategy<Object>  strategy = buildStrategy(expectedObject);

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(strategyExpression);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> it == expectedObject);

        assertEquals(FunctionalIterableImpl.class, instance.getClass());
    }

    @Test
    public void testTakeUntilUsesFunctionalIteratorUntilStrategy() {
        Object expectedObject = new Object();

        FunctionalIteratorStrategy<Object> strategy = buildStrategy(expectedObject);

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(strategyExpression);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> it == expectedObject);

        assertEquals(FunctionalIterableImpl.class, instance.getClass());

        FunctionalIterableImpl<Object> iterableInst = (FunctionalIterableImpl<Object>) instance;

        assertEquals(FunctionalIteratorUntilStrategy.class, iterableInst.strategyExpression.evaluate().getClass());
    }

    @Test
    public void testTakeUntilStopsWhenPredicateIsTrue() {
        Object expectedObject = new Object();

        FunctionalIteratorStrategy<Object> strategy = buildStrategy(expectedObject);

        Expression<FunctionalIteratorStrategy<Object>> strategyExpression = () -> strategy;

        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(strategyExpression);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> true);

        assertEquals(FunctionalIterableImpl.class, instance.getClass());

        FunctionalIterableImpl<Object> iterableInst = (FunctionalIterableImpl<Object>) instance;

        assertEquals(FunctionalIteratorUntilStrategy.class, iterableInst.strategyExpression.evaluate().getClass());

        FunctionalIteratorUntilStrategy<Object> untilStrategy = (FunctionalIteratorUntilStrategy<Object>) iterableInst.strategyExpression.evaluate();

        assertFalse(untilStrategy.hasNextExpression.evaluate());
    }

    @Test
    public void testIteratorStopsWhenPredicateIsTrue() {
        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(this::buildStrategy);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> true);

        Iterator<Object> iterator = instance.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorStopsWhenSourceIsDepleted() {
        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(this::buildDepletedStrategy);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> false);

        Iterator<Object> iterator = instance.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorContinuesWhileSourceIsNotDepletedAndPredicateIsFalse() {
        FunctionalIterableImpl<Object> source = new FunctionalIterableImpl<>(this::buildStrategy);

        FunctionalIterable<Object> instance = source.takeUntil((it) -> false);

        Iterator<Object> iterator = instance.iterator();

        assertTrue(iterator.hasNext());
    }
}
