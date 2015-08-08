package com.hstclair.jaskell.fifth.function;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 8/7/15 9:20 PM
 */
public class TestFunctionList {

    @Test
    public void testConstructFromListOfFunctions() {
        List<Function> functions = new LinkedList<>();
        functions.add(it->it);
        functions.add(it -> it);

        FunctionList<Object, Object> instance = new FunctionList<>(functions);

        assertEquals(functions.size(), instance.functions.size());

        for (int index = 0; index < functions.size(); index++)
            assertEquals(functions.get(index), instance.functions.get(index));
    }

    @Test
    public void testConstructFromFunction() {
        Function<Object, Object> function = (it) -> new Object();

        FunctionList<Object, Object> functionList = new FunctionList<>(function);

        assertEquals(1, functionList.functions.size());
        assertEquals(function, functionList.functions.get(0));
    }

    @Test
    public void testConstructFromTwoFunctions() {
        Function<Object, Object> before = it->it;
        Function<Object, Object> after = it->it;

        FunctionList<Object, Object> instance = new FunctionList<>(before, after);

        assertEquals(2, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after, instance.functions.get(1));
    }

    @Test
    public void testConstructFromFunctionListAndThenFunction() {
        FunctionList<Object, Object> before = new FunctionList<>(it->it, it->it);
        Function<Object, Object> after = it->it;

        FunctionList<Object, Object> instance = new FunctionList<>(before, after);

        assertEquals(3, instance.functions.size());
        assertEquals(before.functions.get(0), instance.functions.get(0));
        assertEquals(before.functions.get(1), instance.functions.get(1));
        assertEquals(after, instance.functions.get(2));
    }

    @Test
    public void testConstructFromFunctionAndThenFunctionList() {
        Function<Object, Object> before = it->it;
        FunctionList<Object, Object> after = new FunctionList<>(it->it, it->it);

        FunctionList<Object, Object> instance = new FunctionList<>(before, after);

        assertEquals(3, instance.functions.size());
        assertEquals(before, instance.functions.get(0));
        assertEquals(after.functions.get(0), instance.functions.get(1));
        assertEquals(after.functions.get(1), instance.functions.get(2));
    }

    @Test
    public void testAppendToListAppendsFunction() {
        List<Function> functions = new LinkedList<>();
        Function<Object, Object> function = (it) -> it;

        FunctionList.appendToList(functions, function);

        assertEquals(1, functions.size());
        assertEquals(function, functions.get(0));
    }

    @Test
    public void testAppendToListAppendsFunctionImpl() {
        Function<Object, Object> function = (it) -> it;
        FunctionImpl<Object, Object> functionImpl = new FunctionImpl<Object, Object>(function);
        List<Function> functions = new LinkedList<>();

        FunctionList.appendToList(functions, functionImpl);

        assertEquals(1, functions.size());
        assertEquals(functionImpl, functions.get(0));
    }

    @Test
    public void testAppendToListAppendsOpaqueFunctionImplementation() {
        class Opaque implements Function<Object, Object> {
            @Override
            public Object apply(Object o) { return o; }
        }

        Opaque opaque = new Opaque();

        List<Function> functions = new LinkedList<>();

        FunctionList.appendToList(functions, opaque);

        assertEquals(1, functions.size());
        assertEquals(opaque, functions.get(0));
    }

    @Test
    public void testAppendToListAppendsMembersOfFunctionList() {
        List<Function> listOfFunctions = new LinkedList<>();
        listOfFunctions.add(it->it);
        listOfFunctions.add(it->it);

        FunctionList<Object, Object> functionList = new FunctionList<>(listOfFunctions);

        List<Function> functions = new LinkedList<>();

        FunctionList.appendToList(functions, functionList);

        assertEquals(functionList.functions.size(), functions.size());

        for (int index = 0; index < functions.size(); index++)
            assertEquals(functionList.functions.get(index), functions.get(index));
    }
}
