package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.data.Tuple;
import com.hstclair.jaskell.fifth.function.Consumer;
import com.hstclair.jaskell.fifth.function.Function;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 7/26/15 2:40 PM
 */
public class TestFunctionalIterator {

    class TestIteratorImpl implements FunctionalIterator<Object> {

        final LinkedList<Object> expected;

        TestIteratorImpl(List<Object> expected) {
            this.expected = new LinkedList<>(expected);
        }

        @Override
        public void forEachRemaining(Consumer<? super Object> action) {
            while (! expected.isEmpty())
                action.accept(expected.remove());
        }

        @Override
        public boolean hasNext() {
            return expected.size() != 0;
        }

        @Override
        public Object next() {
            return expected.remove();
        }
    }

    @Test
    public void testForEachRemainingReturnsAll() {
        Object[] expected = new Object[] { new Object(), new Object(), new Object() };
        final List<Object> result = new LinkedList<>();

        Consumer<Object> consumer = result::add;

        FunctionalIterator<Object> instance = new TestIteratorImpl(Arrays.asList(expected));

        instance.forEachRemaining(consumer);

        assertEquals(expected.length, result.size());

        for (int index = 0; index < expected.length; index++)
            assertEquals(expected[index], result.get(index));
    }

    @Test
    public void testForEachRemainingReturnsRemaining() {
        Object[] expected = new Object[] { new Object(), new Object(), new Object() };
        final List<Object> result = new LinkedList<>();

        Consumer<Object> consumer = result::add;

        FunctionalIterator<Object> instance = new TestIteratorImpl(Arrays.asList(expected));

        assertEquals(expected[0], instance.next());

        instance.forEachRemaining(consumer);

        assertEquals(expected.length - 1, result.size());

        for (int index = 1; index < expected.length; index++)
            assertEquals(expected[index], result.get(index-1));
    }

    @Test
    public void testForEachRemainingReturnsAllJavaConsumer() {
        Object[] expected = new Object[] { new Object(), new Object(), new Object() };
        final List<Object> result = new LinkedList<>();

        java.util.function.Consumer<Object> consumer = result::add;

        FunctionalIterator<Object> instance = new TestIteratorImpl(Arrays.asList(expected));

        instance.forEachRemaining(consumer);

        assertEquals(expected.length, result.size());

        for (int index = 0; index < expected.length; index++)
            assertEquals(expected[index], result.get(index));
    }

    @Test
    public void testForEachRemainingReturnsRemainingJavaConsumer() {
        Object[] expected = new Object[] { new Object(), new Object(), new Object() };
        final List<Object> result = new LinkedList<>();

        java.util.function.Consumer<Object> consumer = result::add;

        FunctionalIterator<Object> instance = new TestIteratorImpl(Arrays.asList(expected));

        assertEquals(expected[0], instance.next());

        instance.forEachRemaining(consumer);

        assertEquals(expected.length - 1, result.size());

        for (int index = 1; index < expected.length; index++)
            assertEquals(expected[index], result.get(index-1));
    }

    boolean match(Object a, Object b) {
        return a == b;
    }

    Function<Object, Boolean> buildMatcher(Object target) {
        return Tuple.curry(this::match).apply(target);
    }

    @Test
    public void testEachUntil() {
        Object notExpected = new Object();
        final List result = new ArrayList<>();
        Object[] expected = new Object[] { new Object(), new Object(), new Object(), notExpected };

        FunctionalIterator<Object> originalInstance = new TestIteratorImpl(Arrays.asList(expected));

        Function<Object, Boolean> matcher = buildMatcher(notExpected);

        FunctionalIterator<Object> instance = originalInstance.eachUntil(matcher);

        Consumer<Object> consumer = result::add;

        instance.forEachRemaining(consumer);

        assertEquals(expected.length - 1, result.size());

        for (int index = 0; index < result.size(); index++)
            assertEquals(expected[index], result.get(index));
    }

    @Test
    public void testEachUntilJavaConsumer() {
        Object notExpected = new Object();
        final List result = new ArrayList<>();
        Object[] expected = new Object[] { new Object(), new Object(), new Object(), notExpected };

        FunctionalIterator<Object> originalInstance = new TestIteratorImpl(Arrays.asList(expected));

        Function<Object, Boolean> matcher = buildMatcher(notExpected);

        FunctionalIterator<Object> instance = originalInstance.eachUntil(matcher);

        java.util.function.Consumer<Object> consumer = result::add;

        instance.forEachRemaining(consumer);

        assertEquals(expected.length - 1, result.size());

        for (int index = 0; index < result.size(); index++)
            assertEquals(expected[index], result.get(index));
    }
}
