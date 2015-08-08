package com.hstclair.jaskell.fifth.demo;

import com.hstclair.jaskell.fifth.data.LzList;
import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Indefinite;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 7/11/15 4:38 PM
 */
public class FifthDebug {

    public static Integer mapFunction(Integer value) {
        Integer result = value * 2;

        return result;
    }

    public static Indefinite<Integer> optionalMapper(Indefinite<Integer> supplier) {
        return supplier.andThen(FifthDebug::mapFunction);
    }

    public static void lzListMap() {
        LzList<Integer> lzList = new LzList<>(new Integer[] {0, 1, 2});

        Expression<LzList<Integer>> listSupplier = lzList.map(FifthDebug::optionalMapper);

        LzList<Integer> list = listSupplier.evaluate();

        int value = list.get(1).evaluate();

        System.out.printf("got %d", value);
    }

    public static void indefiniteAndThen() {
        Indefinite<Integer> optSupInt = Indefinite.of(10);

        Indefinite<Integer> substituted = optSupInt.andThen((value) -> value + 10);

        System.out.printf("returns %d\n", substituted.evaluate());
    }

    public static void debug() {
        lzListMap();
    }
}
