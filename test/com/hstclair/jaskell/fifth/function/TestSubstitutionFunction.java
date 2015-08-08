package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hstclair
 * @since 7/17/15 8:03 PM
 */
public class TestSubstitutionFunction {

    @Test
    public void testConstructFromFunction() {
        Function<Long, Long> expectedFunction = (it) -> it;
        Function<Long, Indefinite<Long>> expectedSubstitutionFunction = (it) -> Indefinite.of(it);

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(expectedSubstitutionFunction, expectedFunction);

        assertEquals(1, instance.functions.size());
        assertEquals(expectedFunction, instance.functions.get(0));
        assertEquals(expectedSubstitutionFunction, instance.substitutionFunction);
    }

    @Test
    public void testConstructFromFunctions() {
        Function<Long, Long> expectedFunctionA = (it) -> it;
        Function<Long, Long> expectedFunctionB = (it) -> it;
        Function<Long, Long> expectedFunctionC = (it) -> it;

        assertNotEquals(expectedFunctionA, expectedFunctionB);
        assertNotEquals(expectedFunctionB, expectedFunctionC);
        assertNotEquals(expectedFunctionC, expectedFunctionA);

        Function<Long, Indefinite<Long>> expectedSubstitutionFunction = (it) -> Indefinite.of(it);

        List<Function> expectedFunctions = new LinkedList<>();

        expectedFunctions.add(expectedFunctionA);
        expectedFunctions.add(expectedFunctionB);
        expectedFunctions.add(expectedFunctionC);

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(expectedSubstitutionFunction, expectedFunctions);

        assertEquals(expectedFunctions.size(), instance.functions.size());

        for (int index = 0; index < expectedFunctions.size(); index++)
            assertEquals(expectedFunctions.get(index), instance.functions.get(index));

        assertEquals(expectedSubstitutionFunction, instance.substitutionFunction);
    }

    @Test
    public void testGetSubstitutionFunctionReturnsSubstitutionFunction() {
        Function<Long, Long> expectedFunction = (it) -> it;
        Function<Long, Indefinite<Long>> expectedSubstitutionFunction = (it) -> Indefinite.of(it);

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(expectedSubstitutionFunction, expectedFunction);

        assertEquals(instance.substitutionFunction, instance.getSubstitutionFunction());
    }

    @Test
    public void testGetFunctionsReturnsFunctions() {
        Function<Long, Long> expectedFunction = (it) -> it;
        Function<Long, Indefinite<Long>> expectedSubstitutionFunction = (it) -> Indefinite.of(it);

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(expectedSubstitutionFunction, expectedFunction);

        assertEquals(instance.functions, instance.getFunctions());
    }

    @Test
    public void testInternalApplyEvaluatesIndefiniteWhenPresent() {
        final Boolean[] executed = {false};

        Long expectedResult = 100l;
        Long expectedInput = -10l;

        Function<Long, Indefinite<Long>> substitutionFunction = (input) -> {
            executed[0] = true;
            assertEquals(expectedInput, input);
            return Indefinite.of(expectedResult);
        };

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(substitutionFunction, (it) -> it);

        List<Function> functions = new LinkedList<>();

        Object result = instance.internalApply(functions, expectedInput);

        assertTrue(executed[0]);
        assertEquals(expectedResult, result);
        assertEquals(0, functions.size());
    }

    @Test
    public void testInternalApplyReturnsFunctionsWhenIndefiniteNotPresent() {
        final Boolean[] executed = {false};

        Long expectedInput = 100l;
        Long expectedResult = expectedInput;

        Function<Long, Long> expectedFunctionA = (it) -> it;
        Function<Long, Long> expectedFunctionB = (it) -> it;
        Function<Long, Long> expectedFunctionC = (it) -> it;

        assertNotEquals(expectedFunctionA, expectedFunctionB);
        assertNotEquals(expectedFunctionB, expectedFunctionC);
        assertNotEquals(expectedFunctionC, expectedFunctionA);

        List<Function> expectedFunctions = new LinkedList<>();

        expectedFunctions.add(expectedFunctionA);
        expectedFunctions.add(expectedFunctionB);
        expectedFunctions.add(expectedFunctionC);

        Function<Long, Indefinite<Long>> substitutionFunction = (input) -> {
            executed[0] = true;
            assertEquals(expectedInput, input);
            return Indefinite.EMPTY;
        };

        SubstitutionFunction<Long, Long> instance = new SubstitutionFunction<>(substitutionFunction, expectedFunctions);

        List<Function> functions = new LinkedList<>();

        Object result = instance.internalApply(functions, expectedInput);

        assertTrue(executed[0]);
        assertEquals(expectedResult, result);
        assertEquals(expectedFunctions.size(), functions.size());

        for (int index = 0; index < expectedFunctions.size(); index++)
            assertEquals(expectedFunctions.get(index), instance.functions.get(index));
    }

    @Test
    public void testComposeReturnsFunctionImpl() {
        SubstitutionFunction<Long, Long> substitutionFunction = new SubstitutionFunction<Long, Long>((it) -> Indefinite.of(it), (it) -> it);

        Function<Long, Long> before = (it) -> it;

        Function<Long, Long> instanceInterface = substitutionFunction.compose(before);

        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) instanceInterface;

        assertEquals(2, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(substitutionFunction, instance.functions.get(1));
    }

    @Test
    public void testAndThenReturnsFunctionImpl() {
        SubstitutionFunction<Long, Long> substitutionFunction = new SubstitutionFunction<Long, Long>((it) -> Indefinite.of(it), (it) -> it);

        Function<Long, Long> after = (it) -> it;

        Function<Long, Long> instanceInterface = substitutionFunction.andThen(after);

        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) instanceInterface;

        assertEquals(2, instance.functions.size());
        assertEquals(substitutionFunction, instance.functions.get(0));
        assertEquals(after, instance.functions.get(1));
    }
}
