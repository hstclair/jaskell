package com.hstclair.jaskell.function;

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
        List<Operation> functions = new LinkedList<>();
        functions.add(it->it);
        functions.add(it -> it);

        OperationList<Object, Object> instance = new OperationList<>(functions);

        assertEquals(functions.size(), instance.operations.size());

        for (int index = 0; index < functions.size(); index++)
            assertEquals(functions.get(index), instance.operations.get(index));
    }

    @Test
    public void testConstructFromTwoFunctions() {
        Function<Object, Object> before = it->it;
        Function<Object, Object> after = it->it;

        OperationList<Object, Object> instance = new OperationList<>(before, after);

        assertEquals(2, instance.operations.size());
        assertEquals(before, instance.operations.get(0));
        assertEquals(after, instance.operations.get(1));
    }

    @Test
    public void testConstructFromFunctionListAndThenFunction() {
        Operation<Object, Object> before1 = (it)-> it;
        Operation<Object, Object> before2 = (it)-> it;
        Operation<Object, Object> before = new OperationList<>(before1, before2);
        Operation<Object, Object> after = it->it;

        OperationList<Object, Object> instance = new OperationList<>(before, after);

        assertEquals(3, instance.operations.size());
        assertEquals(before1, instance.operations.get(0));
        assertEquals(before2, instance.operations.get(1));
        assertEquals(after, instance.operations.get(2));
    }

    @Test
    public void testConstructFromFunctionAndThenFunctionList() {
        Function<Object, Object> before = it->it;
        Function<Object, Object> after1 = it->it;
        Function<Object, Object> after2 = it->it;
        Operation<Object, Object> after = new OperationList<>(after1, after2);

        OperationList<Object, Object> instance = new OperationList<>(before, after);

        assertEquals(3, instance.operations.size());
        assertEquals(before, instance.operations.get(0));
        assertEquals(after1, instance.operations.get(1));
        assertEquals(after2, instance.operations.get(2));
    }

    @Test
    public void testAppendToListAppendsFunction() {
        List<Operation> functions = new LinkedList<>();
        Function<Object, Object> function = (it) -> it;

        OperationList.appendToList(functions, function);

        assertEquals(1, functions.size());
        assertEquals(function, functions.get(0));
    }

//    @Test
//    public void testAppendToListAppendsFunctionImpl() {
//        Function<Object, Object> operation = (it) -> it;
//        EvaluatableFunction<Object, Object> functionImpl = new EvaluatableFunction<Object, Object>(operation);
//        List<Function> operations = new LinkedList<>();
//
//        OperationList.appendToList(operations, functionImpl);
//
//        assertEquals(1, operations.size());
//        assertEquals(functionImpl, operations.get(0));
//    }

    @Test
    public void testAppendToListAppendsOpaqueFunctionImplementation() {
        class Opaque implements Function<Object, Object> {
            @Override
            public Object apply(Object o) { return o; }
        }

        Opaque opaque = new Opaque();

        List<Operation> functions = new LinkedList<>();

        OperationList.appendToList(functions, opaque);

        assertEquals(1, functions.size());
        assertEquals(opaque, functions.get(0));
    }

    @Test
    public void testAppendToListAppendsMembersOfFunctionList() {
        List<Operation> listOfFunctions = new LinkedList<>();
        listOfFunctions.add(it->it);
        listOfFunctions.add(it->it);

        OperationList<Object, Object> operationList = new OperationList<>(listOfFunctions);

        List<Operation> functions = new LinkedList<>();

        OperationList.appendToList(functions, operationList);

        assertEquals(operationList.operations.size(), functions.size());

        for (int index = 0; index < functions.size(); index++)
            assertEquals(operationList.operations.get(index), functions.get(index));
    }
}
