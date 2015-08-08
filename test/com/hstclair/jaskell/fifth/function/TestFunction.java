package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/15/15 5:34 PM
 */
public class TestFunction {

    @Test
    public void testIdentityFunction() {
        Function<Object, Object> identity = Function.identity();

        Object expected = new Object();

        Object result = identity.apply(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testFunctionOfLambdaReturnsLambda() {
        Function<Long, Long> expected = (it) -> it * 2;

        Function instance = Function.of(expected);

        assertEquals(expected, instance);
    }

    @Test
    public void testFunctionOfFunctionImplReturnsFunctionImpl() {
        Function<Long, Long> expected = new FunctionImpl<>( (it) -> it * 2 );

        Function instance = Function.of(expected);

        assertEquals(expected, instance);
    }

    @Test
    public void testFunctionOfSubstitutionFunctionReturnsSubstitutionFunction() {
        Function<Long, Indefinite<Long>> substitution = Indefinite::of;
        Function<Long, Long> original = (it) -> 0l;
        Function<Long, Long> expected = new SubstitutionFunction<Long, Long>(substitution, original);

        Function<Long, Long> instance = Function.of(expected);

        assertEquals(expected, instance);
    }

    @Test
    public void testInvokingFunctionFromJavaFunctionInvokesJavaFunction() {
        boolean[] invoked = {false};
        Object expectedResult[] = {null};
        Object expectedInput[] = {null};

        java.util.function.Function<Object, Object> function = (it) -> {
            assertEquals(expectedInput[0], it);
            invoked[0] = true;
            return expectedResult[0] = new Object();
        };

        Function<Object, Object> instance = Function.from(function);

        assertNotEquals(function, instance);

        expectedInput[0] = new Object();

        Object result = instance.apply(expectedInput[0]);

        assertTrue(invoked[0]);
        assertEquals(expectedResult[0], result);
    }

    @Test
    public void testFunctionAsJavaFunction() {
        boolean[] invoked = {false};
        Object expectedResult[] = {null};
        Object expectedInput[] = {null};

        Function<Object, Object> function = (it) -> {
            assertEquals(expectedInput[0], it);
            invoked[0] = true;
            return expectedResult[0] = new Object();
        };

        java.util.function.Function<Object, Object> instance = function.asJavaFunction();

        assertNotEquals(function, instance);

        expectedInput[0] = new Object();

        Object result = instance.apply(expectedInput[0]);

        assertTrue(invoked[0]);
        assertEquals(expectedResult[0], result);
    }

    @Test
    public void testInvokingComposedFunctionInvokesBothFunctions() {
        boolean[] originalInvoked = {false};
        boolean[] inputModifierInvoked = {false};

        Function<Object, Object> original = (it) -> { originalInvoked[0] = true; return it; };
        Function<Object, Object> inputModifier = (it) -> { inputModifierInvoked[0] = true; return it; };

        Function<Object, Object> instance = original.compose(inputModifier);

        instance.apply(new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(inputModifierInvoked[0]);
    }

    @Test
    public void testInvokingComposedFunctionInvokesInputModifierFunctionWithOriginalInput() {
        boolean[] originalInvoked = {false};
        boolean[] inputModifierInvoked = {false};
        Object[] expected = {null};

        Function<Object, Object> original = (it) -> { originalInvoked[0] = true; return new Object(); };
        Function<Object, Object> inputModifier = (it) -> { assertEquals(expected[0], it); inputModifierInvoked[0] = true; return new Object(); };

        Function<Object, Object> instance = original.compose(inputModifier);

        instance.apply(expected[0] = new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(inputModifierInvoked[0]);
    }

    @Test
    public void testInvokingComposedFunctionInvokesOriginalFunctionWithResultOfInputModifierFunction() {
        boolean[] originalInvoked = {false};
        boolean[] inputModifierInvoked = {false};
        Object[] expected = {null};

        Function<Object, Object> original = (it) -> { assertEquals(expected[0], it); originalInvoked[0] = true; return it; };
        Function<Object, Object> inputModifier = (it) -> { inputModifierInvoked[0] = true; return expected[0] = new Object(); };

        Function<Object, Object> instance = original.compose(inputModifier);

        instance.apply(new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(inputModifierInvoked[0]);
    }

    @Test
    public void testInvokingComposedFunctionReturnsResultOfOriginalFunction() {
        boolean[] originalInvoked = {false};
        boolean[] inputModifierInvoked = {false};
        Object[] expected = {null};

        Function<Object, Object> original = (it) -> { originalInvoked[0] = true; return expected[0] = new Object(); };
        Function<Object, Object> inputModifier = (it) -> { inputModifierInvoked[0] = true; return new Object(); };

        Function<Object, Object> instance = original.compose(inputModifier);

        Object result = instance.apply(new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(inputModifierInvoked[0]);
        assertEquals(expected[0], result);
    }

    @Test
    public void testAndThenInvokesBothFunctions() {
        boolean[] originalInvoked = {false};
        boolean[] resultModifierInvoked = {false};
        Function<Object, Object> originalFunction = (it) -> { originalInvoked[0] = true; return new Object(); };
        Function<Object, Object> resultModifierFunction = (it) -> { resultModifierInvoked[0] = true; return new Object(); };

        Function<Object, Object> instance = originalFunction.andThen(resultModifierFunction);

        assertEquals(instance.getClass(), FunctionImpl.class);

        instance.apply(new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(resultModifierInvoked[0]);
    }

    @Test
    public void testAndThenSuppliesArgumentToOriginalFunction() {
        boolean[] originalInvoked = {false};
        boolean[] resultModifierInvoked = {false};
        Object[] expected = {null};
        Function<Object, Object> originalFunction = (it) -> { originalInvoked[0] = true; assertEquals(expected[0], it);return new Object(); };
        Function<Object, Object> resultModifierFunction = (it) -> { resultModifierInvoked[0] = true; return new Object(); };

        Function<Object, Object> instance = originalFunction.andThen(resultModifierFunction);

        assertEquals(instance.getClass(), FunctionImpl.class);

        expected[0] = new Object();
        instance.apply(expected[0]);

        assertTrue(originalInvoked[0]);
        assertTrue(resultModifierInvoked[0]);
    }

    @Test
    public void testAndThenSuppliesOriginalFunctionResultToModifierFunction() {
        boolean[] originalInvoked = {false};
        boolean[] resultModifierInvoked = {false};
        Object[] expected = {null};
        Function<Object, Object> originalFunction = (it) -> { originalInvoked[0] = true; return expected[0] = new Object(); };
        Function<Object, Object> resultModifierFunction = (it) -> { resultModifierInvoked[0] = true; assertEquals(expected[0], it); return new Object(); };

        Function<Object, Object> instance = originalFunction.andThen(resultModifierFunction);

        assertEquals(instance.getClass(), FunctionImpl.class);

        instance.apply(new Object());

        assertTrue(originalInvoked[0]);
        assertTrue(resultModifierInvoked[0]);
    }

    @Test
    public void testSubstituteIndefiniteReturnsInstanceOfSubstitutionFunction() {
        Function<Object, Object> function = (it) -> it;
        Function<Object, Indefinite<Object>> substituteFunction = Indefinite::of;

        Function<Object, Object> instance = function.substitute(substituteFunction);

        assertEquals(SubstitutionFunction.class, instance.getClass());
    }

    @Test
    public void testSubstituteIndefiniteInvokesBothFunctionsWhenIndefiniteIsEmpty() {
        boolean[] functionInvoked = {false};
        boolean[] substituteInvoked = {false};

        Function<Object, Object> function = (it) -> { functionInvoked[0] = true; return new Object(); };
        Function<Object, Indefinite<Object>> substituteFunction = (it) -> { substituteInvoked[0] = true; return Indefinite.EMPTY; };

        Function<Object, Object> instance = function.substitute(substituteFunction);

        instance.apply(new Object());

        assertTrue(functionInvoked[0]);
        assertTrue(substituteInvoked[0]);
    }

    @Test
    public void testSubstituteIndefiniteReturnsFunctionResultWhenIndefiniteIsEmpty() {
        Object[] expected = {null};

        Function<Object, Object> function = (it) -> { return expected[0] = new Object(); };
        Function<Object, Indefinite<Object>> substituteFunction = (it) -> Indefinite.EMPTY;

        Function<Object, Object> instance = function.substitute(substituteFunction);

        Object result = instance.apply(new Object());

        assertNotNull(result);
        assertEquals(expected[0], result);
    }

    @Test
    public void testSubstituteIndefiniteInvokesOnlySubstituteWhenIndefiniteIsPresent() {
        boolean[] functionNotInvoked = {true};
        boolean[] substituteInvoked = {false};

        Function<Object, Object> function = (it) -> { functionNotInvoked[0] = false; return new Object(); };
        Function<Object, Indefinite<Object>> substituteFunction = (it) -> { substituteInvoked[0] = true; return Indefinite.of(new Object()); };

        Function<Object, Object> instance = function.substitute(substituteFunction);

        instance.apply(new Object());

        assertTrue(functionNotInvoked[0]);
        assertTrue(substituteInvoked[0]);
    }

    @Test
    public void testSubstituteIndefiniteReturnsSubstituteValueWhenIndefiniteIsPresent() {
        Object[] expected = {null};

        Function<Object, Object> function = (it) -> { return new Object(); };
        Function<Object, Indefinite<Object>> substituteFunction = (it) -> { return Indefinite.of(expected[0] = new Object()); };

        Function<Object, Object> instance = function.substitute(substituteFunction);

        Object result = instance.apply(new Object());

        assertNotNull(result);

        assertEquals(expected[0], result);
    }

    @Test
    public void testSubstituteWithRangeReturnsInstanceOfSubstitutionFunctionByRange() {

        Function<Object, Boolean> rangeFunction = (it) -> false;
        Function<Object, Object> function = (it) -> it;
        Function<Object, Object> substituteFunction = (it) -> it;

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        assertEquals(SubstitutionFunctionByRange.class, instance.getClass());
    }

    @Test
    public void testSubstituteWithRangeInvokesOnlyFunctionWhenRangeFunctionReturnsFalse() {
        boolean[] rangeInvoked = {false};
        boolean[] functionInvoked = {false};
        boolean[] substituteNotInvoked = {true};

        Function<Object, Boolean> rangeFunction = (it) -> { rangeInvoked[0] = true; return false; };
        Function<Object, Object> function = (it) -> { functionInvoked[0] = true; return new Object(); };
        Function<Object, Object> substituteFunction = (it) -> { substituteNotInvoked[0] = false; return new Object(); };

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        instance.apply(new Object());

        assertTrue(rangeInvoked[0]);
        assertTrue(functionInvoked[0]);
        assertTrue(substituteNotInvoked[0]);
    }

    @Test
    public void testSubstituteWithRangePassesArgumentToFunctionWhenRangeFunctionReturnsFalse() {
        Object[] expected = {null};

        Function<Object, Boolean> rangeFunction = (it) -> false;
        Function<Object, Object> function = (it) -> { assertNotNull(it); assertEquals(expected[0], it); return new Object(); };
        Function<Object, Object> substituteFunction = (it) -> new Object();

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        instance.apply(expected[0] = new Object());
    }

    @Test
    public void testSubstituteWithRangeReturnsFunctionResultWhenRangeFunctionReturnsFalse() {
        Object[] expected = {null};

        Function<Object, Boolean> rangeFunction = (it) -> false;
        Function<Object, Object> function = (it) -> expected[0] = new Object();
        Function<Object, Object> substituteFunction = (it) -> new Object();

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        Object result = instance.apply(new Object());

        assertEquals(expected[0], result);
    }

    @Test
    public void testSubstituteWithRangeInvokesOnlySubstituteFunctionWhenRangeFunctionReturnsTrue() {
        boolean[] rangeInvoked = {false};
        boolean[] functionNotInvoked = {true};
        boolean[] substituteInvoked = {false};

        Function<Object, Boolean> rangeFunction = (it) -> { rangeInvoked[0] = true; return true; };
        Function<Object, Object> function = (it) -> { functionNotInvoked[0] = false; return new Object(); };
        Function<Object, Object> substituteFunction = (it) -> { substituteInvoked[0] = true; return new Object(); };

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        instance.apply(new Object());

        assertTrue(rangeInvoked[0]);
        assertTrue(functionNotInvoked[0]);
        assertTrue(substituteInvoked[0]);
    }

    @Test
    public void testSubstituteWithRangePassesArgumentToFunctionWhenRangeFunctionReturnsTrue() {
        Object[] expected = {null};

        Function<Object, Boolean> rangeFunction = (it) -> true;
        Function<Object, Object> function = (it) -> new Object();
        Function<Object, Object> substituteFunction = (it) -> { assertNotNull(it); assertEquals(expected[0], it); return new Object(); };

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        instance.apply(expected[0] = new Object());
    }

    @Test
    public void testSubstituteWithRangeReturnsFunctionResultWhenRangeFunctionReturnsTrue() {
        Object[] expected = {null};

        Function<Object, Boolean> rangeFunction = (it) -> true;
        Function<Object, Object> function = (it) -> new Object();
        Function<Object, Object> substituteFunction = (it) -> expected[0] = new Object();

        Function<Object, Object> instance = function.substitute(rangeFunction, substituteFunction);

        Object result = instance.apply(new Object());

        assertEquals(expected[0], result);
    }
}
