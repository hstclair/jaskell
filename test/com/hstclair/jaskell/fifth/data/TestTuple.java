package com.hstclair.jaskell.fifth.data;

import com.hstclair.jaskell.fifth.function.Function;
import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

/**
 * @author hstclair
 * @since 7/11/15 6:52 PM
 */
public class TestTuple {
    @Test
    public void testFirstOfPair() {
        Object thing1 = new Object();
        Object thing2 = new Object();

        Tuple pair = new Tuple(thing1, thing2);

        assertEquals(pair.fst().evaluate(), thing1);
    }

    @Test
    public void testSecondOfPair() {
        Object thing1 = new Object();
        Object thing2 = new Object();

        Tuple pair = new Tuple(thing1, thing2);

        assertEquals(pair.snd().evaluate(), thing2);
    }


    @Test
    public void testFirstOfTriplet() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.fst().evaluate(), thing1);
    }

    @Test
    public void testSecondOfTriplet() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.snd().evaluate(), thing2);
    }

    @Test
    public void testGetFirst() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.get(0).evaluate(), thing1);
    }

    @Test
    public void testGetSecond() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.get(1).evaluate(), thing2);
    }

    @Test
    public void testGetThird() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.get(2).evaluate(), thing3);
    }

    @Test
    public void testGetSupplierFirst() {
        Object thing1 = new Object();
        Object thing2 = new Object();
        Object thing3 = new Object();

        Tuple pair = new Tuple(thing1, thing2, thing3);

        assertEquals(pair.get(() -> 0).evaluate(), thing1);
    }

    @Test
    public void testCurryBiFunction() {
        Double a = 7d;
        Integer b = 2;
        Double c = a * b;

        Function<Double, Function<Integer, Double>> curried1 = Tuple.curry((x, y) -> x * y);

        Function<Integer, Double> curried2 = curried1.apply(a);

        assertEquals(c, curried2.apply(b));
    }

    @Test
    public void testCurryTriFunction() {
        Double a = 7d;
        Integer b = 2;
        String s = "3";
        Double c = a * b * Integer.parseInt(s);

        Function<Double, Function<Integer, Function<String, Double>>> curried1 = Tuple.curry((x, y, z) -> x * y * Integer.parseInt(s));

        Function<Integer, Function<String, Double>> curried2 = curried1.apply(a);

        Function<String, Double> curried3 = curried2.apply(b);

        assertEquals(c, curried3.apply(s));
    }

    @Test
    public void testUncurry() {
        int aVal = 3;
        Double bVal = 4d;
        String expected = String.format("%f", aVal * bVal);

        Function<Integer, Function<Double, String>> curried = (a) -> (b) -> (String.format("%f", a * b));

        BiFunction<Integer, Double, String> uncurried = Tuple.uncurry(curried);

        assertEquals(expected, uncurried.apply(aVal, bVal));
    }



}
