package com.hstclair.jaskell.collections;

import com.hstclair.jaskell.function.Expression;
import com.hstclair.jaskell.function.Function;
import com.hstclair.jaskell.function.RecursionExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author hstclair
 * @since 7/26/15 2:40 PM
 */
public class TestFunctionalIteratorStrategy {

    @Test
    public void testConstructorOneArg() {
        Expression<Object> expected = Object::new;

        FunctionalIteratorStrategy instance = new FunctionalIteratorStrategy<>(expected);

        assertEquals(expected, instance.nextExpression);
        assertEquals(Expression.alwaysTrue, instance.hasNextExpression);
    }

    @Test
    public void testConstructorRejectsNullArg() {

        try {
            new FunctionalIteratorStrategy<>(null);
            fail();
        } catch (NullPointerException ex) { }
    }

    @Test
    public void testConstructorTwoArgs() {
        Expression<Object> expected = Object::new;
        Expression<Boolean> expectedHasNext = () -> true;

        FunctionalIteratorStrategy instance = new FunctionalIteratorStrategy<>(expected, expectedHasNext);

        assertEquals(expected, instance.nextExpression);
        assertEquals(expectedHasNext, instance.hasNextExpression);
    }

    @Test
    public void testConstructorRejectsNullNextExpression() {

        try {
            new FunctionalIteratorStrategy<>(null, Expression.alwaysTrue);
            fail();
        } catch (NullPointerException ex) { }
    }

    @Test
    public void testConstructorRejectsNullHasNextExpression() {

        try {
            new FunctionalIteratorStrategy<>(Object::new, null);
            fail();
        } catch (NullPointerException ex) { }
    }

    @Test
    public void testGetHasNextExpressionSingleArgConstructor() {
        FunctionalIteratorStrategy instance = new FunctionalIteratorStrategy<>(Object::new);

        assertEquals(Expression.alwaysTrue, instance.getHasNextExpression());
    }

    @Test
    public void testGetHasNextExpression() {
        Expression<Boolean> expected = () -> true;

        FunctionalIteratorStrategy instance = new FunctionalIteratorStrategy<>(Object::new, expected);

        assertEquals(expected, instance.getHasNextExpression());
    }

    @Test
    public void testGetNextExpression() {
        Expression<Object> expected = Object::new;

        FunctionalIteratorStrategy instance = new FunctionalIteratorStrategy<>(expected);

        assertEquals(expected, instance.getNextExpression());
    }

    @Test
    public void testRecursionBuildsFunctionalIteratorStrategyFromRecursionExpression() {
        Object expectedSeed = new Object();
        Function<Object, Object> expectedGenerator = (it) -> it;

        FunctionalIteratorStrategy<Object> instance = FunctionalIteratorStrategy.recursion(expectedSeed, expectedGenerator);

        assertEquals(Expression.alwaysTrue, instance.hasNextExpression);
        assertEquals(RecursionExpression.class, instance.nextExpression.getClass());

        RecursionExpression recursionExpression = (RecursionExpression) instance.nextExpression;

        assertEquals(expectedSeed, recursionExpression.current);
        assertEquals(expectedGenerator, recursionExpression.generatorFunction);
    }

    @Test
    public void testUntilBuildsFunctionalIteratorUntilStrategyUsingPredicate() {
        Object expectedSeed = new Object();
        Function<Object, Object> expectedGenerator = (it) -> it;

        FunctionalIteratorStrategy<Object> source = FunctionalIteratorStrategy.recursion(expectedSeed, expectedGenerator);

        Function<Object, Boolean> expectedPredicate = (it) -> true;

        FunctionalIteratorStrategy<Object> instance = source.until(expectedPredicate);

        assertEquals(FunctionalIteratorUntilStrategy.class, instance.getClass());

        FunctionalIteratorUntilStrategy untilStrategy = (FunctionalIteratorUntilStrategy) instance;

        assertEquals(expectedPredicate, untilStrategy.untilPredicate);
    }
}
