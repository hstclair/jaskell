package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Function;
import com.hstclair.jaskell.function.RecursionExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/26/15 2:38 PM
 */
public class TestFunctionalIterable {

    @Test
    public void testOf() {
        Object seed = new Object();

        Function<Object, Object> generator = (a) -> a;

        FunctionalIterable<Object> result = FunctionalIterable.of(seed, generator);

        assertEquals(FunctionalIterableImpl.class, result.getClass());

        FunctionalIterableImpl<Object> instance = (FunctionalIterableImpl<Object>) result;

        FunctionalIteratorStrategy<Object> strategy = instance.strategyExpression.evaluate();

        assertEquals(RecursionExpression.class, strategy.getNextExpression().getClass());

        RecursionExpression<Object> recursionExpression = (RecursionExpression<Object>) strategy.getNextExpression();

        assertEquals(seed, recursionExpression.current);

        assertEquals(generator, recursionExpression.generatorFunction);
    }
}
