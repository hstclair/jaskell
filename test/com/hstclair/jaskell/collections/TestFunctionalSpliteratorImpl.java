package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Consumer;
import com.hstclair.jaskell.function.Expression;
import com.hstclair.jaskell.function.Function;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/26/15 2:42 PM
 */
public class TestFunctionalSpliteratorImpl {

    static <V> V choose(V a, V b) {
        if (a == null) return b;
        return a;
    }

    class Mock extends FunctionalSpliteratorImpl<Object> {

        Function<Consumer<Object>, Boolean> tryAdvanceFunction = (it) -> false;

        Consumer<Consumer<Object>> forEachRemainingConsumer = (it) -> {};

        Expression<FunctionalSpliterator<Object>> trySplitExpression = () -> null;

        Expression<Long> estimateSizeExpression = () -> Long.MAX_VALUE;

        Expression<Integer> characteristicsExpression = () -> FunctionalSpliteratorImpl.DEFAULT_CHARACTERISTICS;

        Expression<Long> getExactSizeExpression;

        Expression<FunctionalComparator<Object>> getComparatorExpression;

        int tryAdvanceInvoked;
        int forEachRemainingInvoked;
        int trySplitInvoked;
        int estimateSizeInvoked;
        int characteristicsInvoked;
        int getExactSizeInvoked;
        int getComparatorInvoked;

        public Mock(FunctionalIteratorStrategy<Object> strategy) {
            super(strategy);

            tryAdvanceFunction = super::tryAdvance;
            forEachRemainingConsumer = super::forEachRemaining;
            trySplitExpression = super::trySplit;
            estimateSizeExpression = super::estimateSize;
            characteristicsExpression = super::characteristics;
            getExactSizeExpression = super::getExactSizeIfKnown;
            getComparatorExpression = super::getComparator;
        }

        public Mock(FunctionalSpliteratorImpl<Object> spliterator,
                    Integer characteristics,
                    Function<Consumer<? super Object>, Boolean> tryAdvanceFunction,
                    Expression<FunctionalSpliterator> trySplitExpression,
                    Expression<Long> estimateSizeExpression,
                    Consumer<Consumer<Object>> forEachRemainingConsumer,
                    Expression<Long> getExactSizeIfKnownExpression,
                    Expression<FunctionalComparator> getComparatorExpression,
                    FunctionalIteratorStrategy<Object> strategy) {

            super(choose(characteristics, spliterator.characteristics),
                  choose(tryAdvanceFunction, spliterator.tryAdvanceFunction),
                  choose(trySplitExpression, spliterator.trySplitExpression),
                  choose(estimateSizeExpression, spliterator.estimateSizeExpression),
                  choose(forEachRemainingConsumer, spliterator.forEachRemainingConsumer),
                  choose(getExactSizeIfKnownExpression, spliterator.getExactSizeIfKnownExpression),
                  choose(getComparatorExpression, spliterator.getComparatorExpression),
                  choose(strategy, spliterator.strategy));

            this.tryAdvanceFunction = super::tryAdvance;
            this.forEachRemainingConsumer = super::forEachRemaining;
            this.trySplitExpression = super::trySplit;
            this.estimateSizeExpression = super::estimateSize;
            this.characteristicsExpression = super::characteristics;
            this.getExactSizeExpression = super::getExactSizeIfKnown;
            this.getComparatorExpression = super::getComparator;
        }



        public int totalMethodsInvoked() {
            return tryAdvanceInvoked + forEachRemainingInvoked + trySplitInvoked + estimateSizeInvoked + characteristicsInvoked;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Object> action) {
            tryAdvanceInvoked++;
            return tryAdvanceFunction.apply(action);
        }

        @Override
        public void forEachRemaining(Consumer<? super Object> action) {
            forEachRemainingInvoked++;
            forEachRemainingConsumer.accept(action);
        }

        @Override
        public FunctionalSpliterator<Object> trySplit() {
            trySplitInvoked++;
            return trySplitExpression.evaluate();
        }

        @Override
        public long estimateSize() {
            estimateSizeInvoked++;
            return estimateSizeExpression.evaluate();
        }

        @Override
        public int characteristics() {
            characteristicsInvoked++;
            return characteristicsExpression.evaluate();
        }

        @Override
        public long getExactSizeIfKnown() {
            getExactSizeInvoked++;
            return getExactSizeExpression.evaluate();
        }

        @Override
        public FunctionalComparator<Object> getComparator() {
            getComparatorInvoked++;
            return getComparatorExpression.evaluate();
        }
    }

    @Test
    public void testConstructorRejectsNull() {

        try {
            new FunctionalSpliteratorImpl<>(null);
            fail();
        } catch (NullPointerException ex) {}
    }


    @Test
    public void testConstructor() {
        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        FunctionalSpliteratorImpl<Object> instance = new Mock(strategy);

        assertEquals(strategy, instance.strategy);
        assertEquals(FunctionalSpliteratorImpl.DEFAULT_CHARACTERISTICS, instance.characteristics);
        assertEquals(FunctionalSpliteratorImpl.defaultTrySplitExpression, instance.trySplitExpression);
        assertEquals(FunctionalSpliteratorImpl.defaultEstimateSizeExpression, instance.estimateSizeExpression);
        assertEquals(FunctionalSpliteratorImpl.defaultGetExactSizeIfKnownExpression, instance.getExactSizeIfKnownExpression);
        assertEquals(FunctionalSpliteratorImpl.defaultGetComparatorExpression, instance.getComparatorExpression);
        assertEquals(FunctionalSpliteratorImpl.defaultForEachRemainingConsumer, instance.forEachRemainingConsumer);
        assertEquals(FunctionalSpliteratorImpl.DefaultTryAdvanceFunction.class, instance.tryAdvanceFunction.getClass());
    }

    @Test
    public void testCharacteristicsMethodReturnsCharacteristics() {

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        int expected = 1;

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              expected,
                                                              null,
                                                              null, null, null, null, null, null);

        int result = instance.characteristics();

        assertEquals(expected, result);
    }

    @Test
    public void testTryAdvanceMethodInvokesTryAdvanceFunction() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        Function<Consumer<? super Object>,Boolean> tryAdvanceFunction = (it) -> { invoked[0] = true; return true; };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              tryAdvanceFunction,
                                                              null, null, null, null, null, null);

        instance.tryAdvance((Consumer<? super Object>) (it) -> {});

        assertTrue(invoked[0]);
    }

    @Test
    public void testTrySplitMethodInvokesTrySplitExpression() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        FunctionalSpliterator<Object> expected = new FunctionalSpliteratorImpl<Object>(strategy);

        Expression<FunctionalSpliterator> trySplitExpression = () -> { invoked[0] = true; return expected; };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              null,
                                                              trySplitExpression, null, null, null, null, null);

        FunctionalSpliterator result = instance.trySplit();

        assertTrue(invoked[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testEstimateSizeMethodInvokesEstimateSizeExpression() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        long expected = 1l;

        Expression<Long> estimateSizeExpression = () -> { invoked[0] = true; return expected; };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              null,
                                                              null, estimateSizeExpression, null, null, null, null);

        long result = instance.estimateSize();

        assertTrue(invoked[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testForEachRemainingMethodInvokesForEachRemainingConsumer() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        Consumer<Object> expected = (it) -> {};

        Consumer<Consumer<Object>> forEachRemainingConsumer = (consumer) -> { invoked[0] = true; assertEquals(expected, consumer); };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              null,
                                                              null, null, forEachRemainingConsumer, null, null, null);

        instance.forEachRemaining(expected);

        assertTrue(invoked[0]);
    }

    @Test
    public void testGetExactSizeIfKnownMethodInvokesGetExactSizeIfKnownExpression() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        long expected = 1l;

        Expression<Long> getExactSizeIfKnownExpression = () -> { invoked[0] = true; return expected; };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              null,
                                                              null, null, null, getExactSizeIfKnownExpression, null, null);

        long result = instance.getExactSizeIfKnown();

        assertTrue(invoked[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testGetComparatorMethodInvokesGetComparatorExpression() {
        Boolean[] invoked = new Boolean[] {false};

        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        FunctionalComparator<Object> expected = new FunctionalComparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        Expression<FunctionalComparator> getComparatorExpression = () -> { invoked[0] = true; return expected; };

        FunctionalSpliteratorImpl<Object> instance = new Mock(new FunctionalSpliteratorImpl<>(strategy),
                                                              null,
                                                              null,
                                                              null, null, null, null, getComparatorExpression, null);

        FunctionalComparator result = instance.getComparator();

        assertTrue(invoked[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultTrySplitExpressionReturnsNull() {
        assertNull(FunctionalSpliteratorImpl.defaultTrySplitExpression.evaluate());
    }

    @Test
    public void testDefaultEstimateSizeExpressionReturnsLongMAX_VALUE() {
        assertEquals(Long.MAX_VALUE, (long) FunctionalSpliteratorImpl.defaultEstimateSizeExpression.evaluate());
    }

    @Test
    public void testDefaultGetExactSizeIfKnownExpressionReturnsNegativeOne() {
        assertEquals(-1l, (long) FunctionalSpliteratorImpl.defaultGetExactSizeIfKnownExpression.evaluate());
    }

    @Test
    public void testDefaultGetComparatorExpressionThrowsIllegalStateException() {
        try {
            FunctionalSpliteratorImpl.defaultGetComparatorExpression.evaluate();
            fail();
        } catch (IllegalStateException ex) { }
    }

    @Test
    public void testDefaultForEachRemainingConsumerThrowsIllegalStateException() {
        try {
            FunctionalSpliteratorImpl.defaultForEachRemainingConsumer.accept((it) -> {});
            fail();
        } catch (IllegalStateException ex) { }
    }

    @Test
    public void testDefaultTryAdvanceFunctionConstructor() {
        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(Object::new, Expression.alwaysTrue);

        FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<Object> instance = new FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<>(strategy);

        assertEquals(strategy.hasNextExpression, instance.hasNextExpression);
        assertEquals(strategy.nextExpression, instance.nextExpression);
    }


    @Test
    public void testDefaultTryAdvanceFunctionApplyExitsWithFalseWhenHasNextExpressionReturnsFalse() {
        Boolean[] invoked = new Boolean[] { false };
        Consumer<Object> neverInvokedConsumer = (it) -> { fail(); };
        Expression<Object> neverInvokedExpression = () -> { fail(); return null; };
        Expression<Boolean> hasNext = () -> { invoked[0] = true; return false; };
        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(neverInvokedExpression, hasNext);

        FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<Object> instance = new FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<>(strategy);

        boolean result = instance.apply(neverInvokedConsumer);

        assertFalse(result);
        assertTrue(invoked[0]);
    }

    @Test
    public void testDefaultTryAdvanceFunctionApplyInvokesNextExpressionWhenHasNextIsTrue() {
        Object[] expected = { null };
        Boolean[] hasNextInvoked = { false };
        Boolean[] nextInvoked = { false };
        Consumer<Object> ignoredConsumer = (it) -> { };
        Expression<Object> nextExpression = () -> { nextInvoked[0] = true; return expected[0] = new Object(); };
        Expression<Boolean> hasNextExpression = () -> { hasNextInvoked[0] = true; return true; };
        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(nextExpression, hasNextExpression);

        FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<Object> instance = new FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<>(strategy);

        boolean result = instance.apply(ignoredConsumer);

        assertTrue(result);
        assertTrue(hasNextInvoked[0]);
        assertTrue(nextInvoked[0]);
    }

    @Test
    public void testDefaultTryAdvanceFunctionApplyPassesResultOfNextExpressionToConsumerWhenHasNextIsTrue() {
        Object[] expected = { null };
        Boolean[] hasNextInvoked = { false };
        Boolean[] nextInvoked = { false };
        Consumer<Object> consumer = (it) -> { assertEquals(expected[0], it); };
        Expression<Object> nextExpression = () -> { nextInvoked[0] = true; return expected[0] = new Object(); };
        Expression<Boolean> hasNextExpression = () -> { hasNextInvoked[0] = true; return true; };
        FunctionalIteratorStrategy<Object> strategy = new FunctionalIteratorStrategy<>(nextExpression, hasNextExpression);

        FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<Object> instance = new FunctionalSpliteratorImpl.DefaultTryAdvanceFunction<>(strategy);

        boolean result = instance.apply(consumer);

        assertTrue(result);
        assertTrue(hasNextInvoked[0]);
        assertTrue(nextInvoked[0]);
    }

}
