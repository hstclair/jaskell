package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Consumer;
import com.hstclair.jaskell.function.Expression;
import com.hstclair.jaskell.function.Function;

import java.util.Objects;


/**
 * @author hstclair
 * @since 7/18/15 2:55 PM
 */
public class FunctionalSpliteratorImpl<T> implements FunctionalSpliterator<T> {

    final static int DEFAULT_CHARACTERISTICS = ORDERED | IMMUTABLE;

    final Function<Consumer<? super T>,Boolean> tryAdvanceFunction;

    final Expression<FunctionalSpliterator> trySplitExpression;

    final Expression<Long> estimateSizeExpression;

    final Consumer<Consumer<Object>> forEachRemainingConsumer;

    final Expression<Long> getExactSizeIfKnownExpression;

    final Expression<FunctionalComparator> getComparatorExpression;

    final FunctionalIteratorStrategy<T> strategy;

    final int characteristics;

//    public FunctionalSpliteratorImpl(Expression<T> iteratorExpression) {
//        Objects.requireNonNull(iteratorExpression);
//
//        this.strategy = null;
//        characteristics = DEFAULT_CHARACTERISTICS;
//        trySplitExpression = FunctionalSpliteratorImpl::defaultTrySplitExpression;
//        estimateSizeExpression = FunctionalSpliteratorImpl::defaultEstimateSizeExpression;
//        getExactSizeIfKnownExpression = FunctionalSpliteratorImpl::defaultGetExactSizeIfKnownExpression;
//        getComparatorExpression = FunctionalSpliteratorImpl::defaultGetComparatorExpression;
//        tryAdvanceFunction = getDefaultIterationExpressionTryAdvance(iteratorExpression);
//        forEachRemainingConsumer = getDefaultForEachRemainingConsumer();
//    }

    public FunctionalSpliteratorImpl(FunctionalIteratorStrategy<T> strategy) {
        this(DEFAULT_CHARACTERISTICS,
             new DefaultTryAdvanceFunction<>(strategy),
             defaultTrySplitExpression,
             defaultEstimateSizeExpression,
             defaultForEachRemainingConsumer,
             defaultGetExactSizeIfKnownExpression,
             defaultGetComparatorExpression,
             strategy);
    }

    FunctionalSpliteratorImpl(int characteristics,
                              Function<Consumer<? super T>,Boolean> tryAdvanceFunction,
                              Expression<FunctionalSpliterator> trySplitExpression,
                              Expression<Long> estimateSizeExpression,
                              Consumer<Consumer<Object>> forEachRemainingConsumer,
                              Expression<Long> getExactSizeIfKnownExpression,
                              Expression<FunctionalComparator> getComparatorExpression,
                              FunctionalIteratorStrategy<T> strategy) {

        this.characteristics = characteristics;
        this.tryAdvanceFunction = tryAdvanceFunction;
        this.trySplitExpression = trySplitExpression;
        this.estimateSizeExpression = estimateSizeExpression;
        this.forEachRemainingConsumer = forEachRemainingConsumer;
        this.getExactSizeIfKnownExpression = getExactSizeIfKnownExpression;
        this.getComparatorExpression = getComparatorExpression;
        this.strategy = Objects.requireNonNull(strategy);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return tryAdvanceFunction.apply(action);
    }

    @Override
    public FunctionalSpliterator<T> trySplit() { return trySplitExpression.evaluate(); }

    @Override
    public long estimateSize() {
        return estimateSizeExpression.evaluate();
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        forEachRemainingConsumer.accept((Consumer<Object>) action);
    }

    @Override
    public long getExactSizeIfKnown() {
        return getExactSizeIfKnownExpression.evaluate();
    }

    @Override
    public FunctionalComparator<T> getComparator() { return getComparatorExpression.evaluate(); }


    static final Expression<FunctionalSpliterator> defaultTrySplitExpression = () -> null;

    static final Expression<Long> defaultEstimateSizeExpression = () -> Long.MAX_VALUE;

    static final Expression<Long> defaultGetExactSizeIfKnownExpression = () -> -1l;

    static final Expression<FunctionalComparator> defaultGetComparatorExpression = () -> {
        throw new IllegalStateException("unsorted");
    };

//    Function<Consumer<? super T>, Boolean> getDefaultIterationExpressionTryAdvance(Expression<T> iterationExpression) {
//        return (operation) -> {
//
//            T value = iterationExpression.evaluate();
//
//            operation.accept(value);
//
//            return true;
//        };
//    }

    static class DefaultTryAdvanceFunction<T> implements Function<Consumer<? super T>, Boolean> {

        final Expression<Boolean> hasNextExpression;
        final Expression<T> nextExpression;

        DefaultTryAdvanceFunction(FunctionalIteratorStrategy<T> strategy) {
            this.nextExpression = strategy.nextExpression;
            this.hasNextExpression = strategy.hasNextExpression;
        }

        @Override
        public Boolean apply(Consumer<? super T> operation) {
            if (! hasNextExpression.evaluate()) return false;

            operation.accept(nextExpression.evaluate());

            return true;
        }
    }

//    Function<Consumer<? super T>, Boolean> getDefaultStrategyTryAdvance(FunctionalIteratorStrategy<T> strategy) {
//        Expression<Boolean> hasNextExpression = strategy.getHasNextExpression();
//        Expression<T> nextExpression = strategy.getNextExpression();
//
//        return (operation) -> {
//            if (! hasNextExpression.evaluate()) return false;
//
//            operation.accept(nextExpression.evaluate());
//
//            return true;
//        };
//    }

    static final Consumer<Consumer<Object>> defaultForEachRemainingConsumer = (it) -> { throw new IllegalStateException("iteration is unbounded"); };
}
