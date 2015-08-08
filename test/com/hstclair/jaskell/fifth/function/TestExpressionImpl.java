package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/11/15 11:42 PM
 */
public class TestExpressionImpl {

    @Test
    public void testConstructFromLambda() {
        Long expected = 100l;

        Expression<Long> lambda = () -> expected;

        ExpressionImpl<Long> expressionImpl = new ExpressionImpl<Long>(lambda);

        assertEquals(expected, expressionImpl.evaluate());
    }

    @Test
    public void testConstructFromLambdaAndTransformation() {
        Long expected = 100l;
        String input = expected.toString();
        Expression<String> lambda = () -> input;
        Function<String, Long> transformation = Long::parseLong;

        ExpressionImpl<Long> expressionImpl = new ExpressionImpl<Long>(lambda, transformation);

        assertNotEquals(lambda, expressionImpl);
        assertEquals(expected, expressionImpl.evaluate());
    }

    @Test
    public void testConstructFromLambdaAndTransformations() {
        String input = "100";
        String expected = "200";
        Expression<String> lambda = () -> input;
        Function<String, Long> transformation1 = Long::parseLong;
        Function<Long, Long> transformation2 = (it) -> it * 2;
        Function<Long, String> transformation3 = (it) -> Long.toString(it);

        List<Function> transformations = new LinkedList<>();
        transformations.add(transformation1);
        transformations.add(transformation2);
        transformations.add(transformation3);

        ExpressionImpl<String> expressionImpl = new ExpressionImpl<String>(lambda, transformations);

        assertNotEquals(lambda, expressionImpl);
        assertEquals(expected, expressionImpl.evaluate());
    }

    @Test
    public void testFindStartingExpressionFindsExpression() {
        Expression<Long> expected = () -> 100l;

        ExpressionImpl<Long> expressionImpl = new ExpressionImpl<Long>(expected);

        ExpressionImpl<Long> expressionImplexpressionImpl = new ExpressionImpl<Long>(expressionImpl);

        LinkedList<Function> transformations = new LinkedList<>();

        Expression result = ExpressionImpl.findStartingExpression(expressionImplexpressionImpl, transformations);

        assertEquals(expected, result);
        assertTrue(transformations.isEmpty());
    }

    @Test
    public void testFindStartingExpressionFindsTransformations() {
        Long expectedLong = 100l;

        Expression<Long> expected = () -> expectedLong * 4;

        Function<Long, String> a = (x) -> Long.toString(x / 2);
        Function<String, Double> b = (x) -> Double.parseDouble(x) / 2;
        Function<Double, Long> c = (x) -> (long) ((double) x);

        assertNotEquals(a, b);
        assertNotEquals(b, c);
        assertNotEquals(c, a);

        ExpressionImpl<String> expressionImpl = new ExpressionImpl<>(expected, a);

        ExpressionImpl<Double> expressionImplexpressionImpl = new ExpressionImpl<>(expressionImpl, b);

        ExpressionImpl<Long> expressionImplFinal = new ExpressionImpl<>(expressionImplexpressionImpl, c);

        LinkedList<Function> transformations = new LinkedList<>();

        Expression result = ExpressionImpl.findStartingExpression(expressionImplFinal, transformations);

        assertEquals(expected, result);
        assertEquals(3, transformations.size());

        assertEquals(a, transformations.get(0));
        assertEquals(b, transformations.get(1));
        assertEquals(c, transformations.get(2));

        assertEquals(expectedLong, expressionImplFinal.evaluate());
    }

    class InvocationTest<T> implements Expression<T> {

        final Expression<T> expression;

        boolean invoked = false;

        InvocationTest(Expression<T> expression) {
            this.expression = expression;
        }

        @Override
        public T evaluate() {
            invoked = true;

            return expression.evaluate();
        }
    }

    @Test
    public void testFindStartingExpressionPerformsSubstitution() {

        boolean invoked = false;

        Expression<Long> notExpected = () -> -100l;
        Expression<Long> expected = () -> 100l;

        InvocationTest<Long> invocationTest = new InvocationTest<>(expected);

        Function<Long, Long> a = (x) -> x + 1;
        Function<Long, Long> b = (x) -> x - 1;
        Function<Long, Long> c = (x) -> x;

        assertNotEquals(a, b);
        assertNotEquals(b, c);
        assertNotEquals(c, a);

        ExpressionImpl<Long> alsoNotExpected = new ExpressionImpl<Long>(notExpected, a);

        Indefinite<Long> indefinite = Indefinite.of(invocationTest);

        SubstituteExpressionImpl<Long> substituteExpression = new SubstituteExpressionImpl<Long>(alsoNotExpected, indefinite);

        ExpressionImpl<Long> expressionImplFinal = new ExpressionImpl<Long>(substituteExpression, c);


        LinkedList<Function> transformations = new LinkedList<>();

        Expression result = ExpressionImpl.findStartingExpression(expressionImplFinal, transformations);

        assertNotEquals(notExpected, result);
        assertNotEquals(alsoNotExpected, result);
        assertEquals(expected.evaluate(), result.evaluate());
        assertTrue(invocationTest.invoked);
    }

    @Test
    public void testFindStartingExpressionDropsSubstitutedFunctions() {

        Expression<Long> notExpected = () -> -100l;
        Expression<Long> expected = () -> 100l;

        Function<Long, Long> a = (x) -> x + 1;
        Function<Long, Long> b = (x) -> x - 1;
        Function<Long, Long> c = (x) -> x;

        assertNotEquals(a, b);
        assertNotEquals(b, c);
        assertNotEquals(c, a);

        ExpressionImpl<Long> alsoNotExpected = new ExpressionImpl<Long>(notExpected, a);

        SubstituteExpressionImpl<Long> expressionImplexpressionImpl = new SubstituteExpressionImpl<Long>(alsoNotExpected, Indefinite.of(expected));

        ExpressionImpl<Long> expressionImplFinal = new ExpressionImpl<Long>(expressionImplexpressionImpl, c);

        LinkedList<Function> transformations = new LinkedList<>();

        Expression result = ExpressionImpl.findStartingExpression(expressionImplFinal, transformations);

        assertEquals(expected.evaluate(), result.evaluate());
        assertEquals(1, transformations.size());

        assertEquals(c, transformations.get(0));
    }

    @Test
    public void testAndThen() {
        Long expected = 100l;

        ExpressionImpl<Long> expression = new ExpressionImpl<>(() -> expected / 4);

        Expression<Long> andThen = expression.andThen((it) -> it * 4);

        Long result = andThen.evaluate();

        assertEquals(expected, result);
    }

    @Test
    public void testEvaluateWithSubstitutionAndNestedExpressions() {
        Long input = 100l;

        Expression<Long> notExpected = () -> -input;
        Expression<Long> alternateExpression = () -> input;

        InvocationTest<Long> invocationTest = new InvocationTest<>(alternateExpression);

        Function<Long, String> a = (x) -> Long.toString(x + 10);
        Function<String, Double> b = (x) -> Double.parseDouble(x) - 1;
        Function<Long, String> c = (x) -> Long.toString(x);

        assertNotEquals(a, b);
        assertNotEquals(b, c);
        assertNotEquals(c, a);

        ExpressionImpl<String> expression1Transformed = new ExpressionImpl<String>(notExpected, c);

        ExpressionImpl<String> expression2Transformed = new ExpressionImpl<String>(invocationTest, a);

        SubstituteExpressionImpl<String> substituteExpression = new SubstituteExpressionImpl<String>(expression1Transformed, Indefinite.of(expression2Transformed));

        ExpressionImpl<Double> instance = new ExpressionImpl<Double>(substituteExpression, b);

        LinkedList<Function> transformations = new LinkedList<>();

        Expression result = ExpressionImpl.findStartingExpression(instance, transformations);

        assertEquals(alternateExpression.evaluate(), result.evaluate());

        assertTrue(invocationTest.invoked);

        assertEquals(2, transformations.size());

        assertEquals(a, transformations.get(0));

        assertEquals(b, transformations.get(1));

        Double expected = b.apply(a.apply(alternateExpression.evaluate()));

        assertEquals(expected, instance.evaluate());
    }
}
