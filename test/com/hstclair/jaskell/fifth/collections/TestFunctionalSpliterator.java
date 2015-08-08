package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.function.Consumer;
import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Function;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/26/15 2:42 PM
 */
public class TestFunctionalSpliterator {

    class Mock implements FunctionalSpliterator<Object> {

        Function<Consumer<Object>, Boolean> tryAdvanceFunction = (it) -> false;

        Consumer<Consumer<Object>> forEachRemainingConsumer = (it) -> {};

        Expression<Spliterator<Object>> trySplitExpression = () -> null;

        Expression<Long> estimateSizeExpression = () -> Long.MAX_VALUE;

        Expression<Integer> characteristicsExpression = () -> FunctionalSpliteratorImpl.DEFAULT_CHARACTERISTICS;

        int tryAdvanceInvoked;
        int forEachRemainingInvoked;
        int trySplitInvoked;
        int estimateSizeInvoked;
        int characteristicsInvoked;

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
        public Spliterator<Object> trySplit() {
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
    }


    @Test
    public void testTryAdvanceJavaConsumer() {
        int[] invoked = new int[] { 0 };

        Object expected = new Object();

        java.util.function.Consumer<Object> consumer = (it) -> { invoked[0]++; assertEquals(expected, it); };

        Mock instance = new Mock();

        instance.tryAdvanceFunction = (it) -> { it.accept(expected); return false; };

        instance.tryAdvance(consumer);

        assertEquals(1, instance.tryAdvanceInvoked);
        assertEquals(1, instance.totalMethodsInvoked());
        assertEquals(1, invoked[0]);
    }

    @Test
    public void testForEachRemainingJavaConsumer() {
        int[] invoked = new int[] { 0 };

        Object expected = new Object();

        java.util.function.Consumer<Object> consumer = (it) -> { invoked[0]++; assertEquals(expected, it); };

        Mock instance = new Mock();

        instance.forEachRemainingConsumer = (it) -> it.accept(expected);

        instance.forEachRemaining(consumer);

        assertEquals(1, instance.forEachRemainingInvoked);
        assertEquals(1, instance.totalMethodsInvoked());
        assertEquals(1, invoked[0]);
    }
}
