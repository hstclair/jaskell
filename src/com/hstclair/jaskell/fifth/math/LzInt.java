//package com.hstclair.jaskell.fifth.math;
//
//import com.hstclair.jaskell.fifth.data.LzList;
//import com.hstclair.jaskell.fifth.function.Expression;
//
//import java.util.function.Supplier;
//
///**
// * @author hstclair
// * @since 7/5/15 1:10 PM
// */
//public class LzInt {
//    public final static LzInt ZERO = new LzInt();
//
//    public final static LzInt ONE = new LzInt(1);
//
//    public final static LzInt NEGATIVEONE = new LzInt(-1);
//
//    // array of unsigned integer suppliers that represent the value of this LzInt
//    // value[0] represents the least significant int
//    final LzList<Integer> value;
//
//    private LzInt() {
//        value = LzList.EMPTY;
//    }
//
//    LzInt(LzList<Integer> value) {
//        this.value = value;
//    }
//
//    private LzInt(long value) {
//        this.value = new LzList<>( new Integer[] { leastSignificantInt(value), mostSignificantInt(value) } );
//    }
//
//    private static int leastSignificantInt(long value) {
//        return (int) value;
//    }
//
//    private static int mostSignificantInt(long value) {
//        return (int) (value >> 32);
//    }
//
//    public static LzInt toLzInt(long value) {
//        if (value == 0)
//            return ZERO;
//
//        if (value == 1)
//            return ONE;
//
//        if (value == -1)
//            return NEGATIVEONE;
//
//        return new LzInt(value);
//    }
//
//    public Expression<Integer> sign() {
//        return () ->  (value.last().evaluate() < 0) ? -1 : 1;
//    }
//
//    public Expression<LzInt> sum(Supplier<LzInt> addend) {
//        return LzIntSumBuilder.sum(value, addend.get().value);
//    }
//
//}
