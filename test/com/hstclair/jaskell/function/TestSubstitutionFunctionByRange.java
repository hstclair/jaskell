package com.hstclair.jaskell.function;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/17/15 9:59 PM
 */
public class TestSubstitutionFunctionByRange {

    @Test
    public void testConstructFromFunction() {
        Function<Long, Boolean> rangeFunction = (it) -> true;
        Function<Long, Long> substituteFunction = (it) -> it;
        Function<Long, Long> originalFunction = (it) -> it;

        SubstitutionFunctionByRange<Long, Long> instance = new SubstitutionFunctionByRange<Long, Long>(rangeFunction, substituteFunction, originalFunction);

        assertEquals(rangeFunction, instance.rangeFunction);
        assertEquals(substituteFunction, instance.substituteFunction);
        assertEquals(originalFunction, instance.functions.get(0));
    }

    @Test
    public void testConstructFromFunctionList() {
        Function<Long, Boolean> rangeFunction = (it) -> true;
        Function<Long, Long> substituteFunction = (it) -> it;
        Function<Long, Long> originalFunctionA = (it) -> it;
        Function<Long, Long> originalFunctionB = (it) -> it;
        Function<Long, Long> originalFunctionC = (it) -> it;

        List<Function> expectedFunctions = new LinkedList<>();

        expectedFunctions.add(originalFunctionA);
        expectedFunctions.add(originalFunctionB);
        expectedFunctions.add(originalFunctionC);

        SubstitutionFunctionByRange<Long, Long> instance = new SubstitutionFunctionByRange<Long, Long>(rangeFunction, substituteFunction, expectedFunctions);

        assertEquals(rangeFunction, instance.rangeFunction);
        assertEquals(substituteFunction, instance.substituteFunction);
        assertEquals(expectedFunctions.size(), instance.functions.size());
        for (int index = 0; index < instance.functions.size(); index++)
            assertEquals(expectedFunctions.get(index), instance.functions.get(index));
    }

    @Test
    public void testInternalApplyReturnsValueAndSubstituteFunctionWhenInRange() {
        Function<Long, Boolean> rangeFunction = (it) -> true;
        Function<Long, Long> substituteFunction = (it) -> it;
        Function<Long, Long> originalFunction = (it) -> it;

        SubstitutionFunctionByRange<Long, Long> instance = new SubstitutionFunctionByRange<Long, Long>(rangeFunction, substituteFunction, originalFunction);

        List<Function> functions = new LinkedList<>();

        Long expected = 1l;
        Long result = (Long) instance.internalApply(functions, expected);

        assertEquals(expected, result);
        assertEquals(1, functions.size());
        assertEquals(substituteFunction, functions.get(0));
    }

    @Test
    public void testInternalApplyReturnsValueAndOriginalFunctionsWhenNotInRange() {
        Function<Long, Boolean> rangeFunction = (it) -> false;
        Function<Long, Long> substituteFunction = (it) -> it;
        Function<Long, Long> originalFunctionA = (it) -> it;
        Function<Long, Long> originalFunctionB = (it) -> it;
        Function<Long, Long> originalFunctionC = (it) -> it;

        List<Function> expectedFunctions = new LinkedList<>();

        expectedFunctions.add(originalFunctionA);
        expectedFunctions.add(originalFunctionB);
        expectedFunctions.add(originalFunctionC);

        SubstitutionFunctionByRange<Long, Long> instance = new SubstitutionFunctionByRange<Long, Long>(rangeFunction, substituteFunction, expectedFunctions);

        List<Function> functions = new LinkedList<>();

        Long expected = 1l;
        Long result = (Long) instance.internalApply(functions, expected);

        assertEquals(expected, result);
        assertEquals(expectedFunctions.size(), functions.size());
        for (int index = 0; index < functions.size(); index++)
            assertEquals(expectedFunctions.get(index), functions.get(index));
    }

    @Test
    public void testComposeReturnsFunctionImplAndThisAsSecondFunction() {
        Function<Long, Long> before = (it) -> it;

        SubstitutionFunctionByRange<Long, Long> after = new SubstitutionFunctionByRange<>(it->Boolean.TRUE, it->it, it->it);

        Function<Long, Long> composed = after.compose(before);

        assertEquals(FunctionImpl.class, composed.getClass());

        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) composed;

        assertEquals(2, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after, instance.functions.get(1));
    }

    @Test
    public void testAndThenReturnsFunctionImplAndThisAsFirstFunction() {
        Function<Long, Long> after = (it) -> it;

        SubstitutionFunctionByRange<Long, Long> before = new SubstitutionFunctionByRange<>(it->Boolean.TRUE, it->it, it->it);

        Function<Long, Long> composed = before.andThen(after);

        assertEquals(FunctionImpl.class, composed.getClass());

        FunctionImpl<Long, Long> instance = (FunctionImpl<Long, Long>) composed;

        assertEquals(2, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after, instance.functions.get(1));
    }

}
