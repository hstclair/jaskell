package com.hstclair.jaskell.fifth.function;

import com.sun.javafx.UnmodifiableArrayList;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/15/15 6:42 PM
 */
public class TestFunctionImpl {

    @Test
    public void testConstructFromSubstitutionFunction() {

        long crossover = 100l;

        Function<Long, Long> baseline = (it) -> it * 2;
        Function<Long, Long> belowCrossover = (it) -> it * -1;

        Long above = crossover + 1;
        Long below = crossover - 1;

        Long expectedAbove = baseline.apply(above);
        Long expectedBelow = belowCrossover.apply(below);

        Function<Long, Indefinite<Long>> substitute = (it) -> it < crossover ? Indefinite.of(belowCrossover.apply(it)) : Indefinite.EMPTY;

        Function<Long, Long> substituted = baseline.substitute(substitute);

        Function<Long, Long> instance = new FunctionImpl<>(substituted);

        assertEquals(FunctionImpl.class, instance.getClass());
        assertEquals(expectedAbove, instance.apply(above));
        assertEquals(expectedBelow, instance.apply(below));
    }

    @Test
    public void testConstructFromFunctionList() {
        Function<Long, Long> stage1 = (it) -> it * 2;
        Function<Long, Double> stage2 = (it) -> (double) (it * 3);

        Long input = 10l;
        Double expected = stage2.apply(stage1.apply(input));

        List<Function> functions = new LinkedList<>();
        functions.add(stage1);
        functions.add(stage2);

        FunctionImpl<Long, Double> instance = new FunctionImpl<Long, Double>(functions);

        assertEquals(functions.size(), instance.functions.size());
        assertEquals(functions.get(0), instance.functions.get(0));
        assertEquals(functions.get(1), instance.functions.get(1));
        assertEquals(expected, instance.apply(input));

        try {
            instance.functions.add((it) -> it);
        } catch (UnsupportedOperationException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructFromBeforeAndAfterFunctions() {
        Function<Long, Long> before = (it) -> it * 2;
        Function<Long, Double> after = (it) -> (double) (it * 3);

        Long input = 10l;
        Double expected = after.apply(before.apply(input));

        FunctionImpl<Long, Double> instance = new FunctionImpl<>(before, after);

        assertEquals(2, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after, instance.functions.get(1));
        assertEquals(expected, instance.apply(input));

        try {
            instance.functions.add((it) -> it);
        } catch (UnsupportedOperationException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructFromBeforeFunctionListAndAfterFunction() {
        Function<Long, Long> before1 = (it) -> it * 2;
        Function<Long, Long> before2 = (it) -> it * 3;
        Function<Long, Double> after = (it) -> (double) (it * 5);

        Long input = 10l;
        Double expected = after.apply(before2.apply(before1.apply(input)));

        List<Function> before = new LinkedList<>();
        before.add(before1);
        before.add(before2);

        FunctionImpl<Long, Double> instance = new FunctionImpl<>(before, after);

        assertEquals(3, instance.functions.size());
        assertEquals(before1, instance.functions.get(0));
        assertEquals(before2, instance.functions.get(1));
        assertEquals(after, instance.functions.get(2));
        assertEquals(expected, instance.apply(input));

        try {
            instance.functions.add((it) -> it);
        } catch (UnsupportedOperationException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructFromBeforeFunctionAndAfterFunctionList() {
        Function<Long, Long> after1 = (it) -> it * 2;
        Function<Long, Long> after2 = (it) -> it * 3;
        Function<Double, Long> before = (it) -> (long) (it * 5);

        Double input = 10d;
        Long expected = after2.apply(after1.apply(before.apply(input)));

        List<Function> after = new LinkedList<>();
        after.add(after1);
        after.add(after2);

        FunctionImpl<Double, Long> instance = new FunctionImpl<>(before, after);

        assertEquals(3, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after1, instance.functions.get(1));
        assertEquals(after2, instance.functions.get(2));
        assertEquals(expected, instance.apply(input));

        try {
            instance.functions.add((it) -> it);
        } catch (UnsupportedOperationException ex) {
            return;
        }

        fail();
    }

    @Test
    public void testAddToFunctionsFunctionImpl() {
        Function<Long, Long> expected = (it) -> it;

        Function<Long, Long> function = new FunctionImpl<>(expected);

        List<Function> functions = new LinkedList<>();

        FunctionImpl.addToFunctions(function, functions);

        assertEquals(1, functions.size());
        assertEquals(expected, functions.get(0));
    }

    @Test
    public void testAddToFunctionsLambda() {
        Function<Long, Long> expected = (it) -> it;

        List<Function> functions = new LinkedList<>();

        FunctionImpl.addToFunctions(expected, functions);

        assertEquals(1, functions.size());
        assertEquals(expected, functions.get(0));
    }

    @Test
    public void testAddToFunctionsSubstitutionFunction() {

        long crossover = 100l;

        Function<Long, Long> baseline = (it) -> it * 2;
        Function<Long, Long> belowCrossover = (it) -> it * -1;

        Function<Long, Indefinite<Long>> substitute = (it) -> it < crossover ? Indefinite.of(belowCrossover.apply(it)) : Indefinite.EMPTY;

        Function<Long, Long> expected = baseline.substitute(substitute);

        List<Function> functions = new LinkedList<>();

        FunctionImpl.addToFunctions(expected, functions);

        assertEquals(1, functions.size());
        assertEquals(expected, functions.get(0));
    }

    @Test
    public void testInternalApply() {
        Function<Long, Long> expectedFunction = (in) -> in;

        FunctionImpl<Long, Long> instance = new FunctionImpl<Long, Long>(expectedFunction);

        Object expectedObject = new Object();

        List<Function> functions = new LinkedList<>();

        Object resultObject = instance.internalApply(functions, expectedObject);

        assertEquals(functions.size(), 1);
        assertEquals(expectedObject, resultObject);
        assertEquals(expectedFunction, functions.get(0));
    }

    @Test
    public void testApplyFunctionNullFunction() {

        FunctionImpl<Long, Long> instance = new FunctionImpl<Long, Long>((in)->in);

        List<Function> functions = new LinkedList<>();
        functions.add(null);

        Object expectedObject = new Object();

        Object resultObject = instance.applyFunction(functions, expectedObject);

        assertEquals(functions.size(), 0);
        assertEquals(expectedObject, resultObject);
    }

    @Test
    public void testApplyFunctionSimpleFunction() {
        Function<Long, Long> function = (in) -> in * 30l;

        Long input = 100l;
        Long expected = function.apply(input);

        FunctionImpl<Long, Long> instance = new FunctionImpl<Long, Long>((in)->in);

        List<Function> functions = new LinkedList<>();
        functions.add(function);

        Long result = (Long) instance.applyFunction(functions, input);

        assertEquals(functions.size(), 0);
        assertEquals(expected, result);
    }

    @Test
    public void testApplyFunctionFunctionImpl() {
        List<Function> functionList = new LinkedList<>();
        Object expected = new Object();

        final boolean[] called = {false};

        FunctionImpl<Long, Long> function = new FunctionImpl<Long, Long>((in)->in) {

            @Override
            Object internalApply(List<Function> functions, Object value) {
                assertEquals(functionList, functions);
                assertEquals(expected, value);

                called[0] = true;

                return value;
            }
        };

        functionList.add(function);

        Object result = function.applyFunction(functionList, expected);

        assertTrue(called[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testApplyImpl() {
        Object expected = new Object();

        final boolean[] called = {false};

        FunctionImpl<Object, Object> function = new FunctionImpl<Object, Object>((in)->in) {

            @Override
            Object applyFunction(List<Function> functions, Object value) {
                assertEquals(1, functions.size());
                assertEquals(this, functions.get(0));
                assertEquals(expected, value);

                functions.remove(0);

                called[0] = true;

                return value;
            }
        };

        Object result = function.applyImpl(function, expected);

        assertTrue(called[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testApply() {
        Object expected = new Object();

        final boolean[] called = {false};

        FunctionImpl<Object, Object> function = new FunctionImpl<Object, Object>((in)->in) {

            @Override
            Object applyImpl(Function function, Object value) {
                assertEquals(this, function);
                assertEquals(expected, value);

                called[0] = true;

                return value;
            }
        };

        Object result = function.applyImpl(function, expected);

        assertTrue(called[0]);
        assertEquals(expected, result);
    }

    @Test
    public void testCompose() {

        List<Function> functions = new LinkedList<>();

        Function<Long, Long> before1 = (in)->in;
        Function<Long, Long> before2 = (in)->in*2;
        Function<Long, Long> before3 = (in)->in*3;
        Function<Long, Long> after = (in)->in*5;

        Long input = 100l;
        Long expected = after.apply(before3.apply(before2.apply(before1.apply(input))));

        functions.add(before1);
        functions.add(before2);
        functions.add(before3);

        FunctionImpl<Long, Long> function = new FunctionImpl<>(functions);


        Function<Long, Long> result = function.andThen(after);
        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) result;

        assertTrue(instance.functions.containsAll(functions));
        assertEquals(after, instance.functions.get(instance.functions.size() - 1));
        assertEquals(expected, instance.apply(input));
    }

    @Test
    public void testAndThen() {

        List<Function> functions = new LinkedList<>();

        Function<Long, Long> after1 = (in)->in;
        Function<Long, Long> after2 = (in)->in*2;
        Function<Long, Long> after3 = (in)->in*3;
        Function<Long, Long> before = (in)->in*5;

        Long input = 100l;
        Long expected = after3.apply(after2.apply(after1.apply(before.apply(input))));

        functions.add(after1);
        functions.add(after2);
        functions.add(after3);

        FunctionImpl<Long, Long> function = new FunctionImpl<>(functions);


        Function<Long, Long> result = function.compose(before);
        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) result;

        assertTrue(instance.functions.containsAll(functions));
        assertEquals(before, instance.functions.get(0));
        assertEquals(expected, instance.apply(input));
    }

}
