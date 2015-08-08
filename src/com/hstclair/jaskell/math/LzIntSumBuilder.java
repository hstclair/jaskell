//package com.hstclair.jaskell.math;
//
//import com.hstclair.jaskell.function.Expression;
//import com.hstclair.jaskell.data.LzList;
//import com.hstclair.jaskell.function.Indefinite;
//import com.hstclair.jaskell.function.Function;
//
//
///**
// * @author hstclair
// * @since 7/9/15 8:55 PM
// */
//public class LzIntSumBuilder {
//
//    static LzList<Integer> integerList(LzList<SumAndCarry> resultIteration) {
//        Function<Integer, Indefinite<Integer>> getFunction = resultIteration.getFunction.andThen(optional -> optional.andThen(SumAndCarry::getSum));
//        Expression<Boolean> isFiniteSupplier = () -> true;
//        Indefinite<Integer> lengthSupplier = Indefinite.of(() -> {
//            int len = 0;
//
//            while (getFunction.apply(len).isPresent())
//                len++;
//
//            return len;
//        });
//
//        return new LzList<>(getFunction, lengthSupplier, isFiniteSupplier);
//    }
//
//    public static Expression<LzInt> sum(LzList<Integer> lValue, LzList<Integer> rValue) {
//        return () -> {
//            SumAndCarry firstElement = new SumAndCarry(0, () -> lValue.get(0).evaluate(), () -> rValue.get(0).evaluate(), ()->0l);
//
//            LzList<SumAndCarry> resultIteration = LzList.iteration(last -> new SumAndCarry(last, lValue, rValue), firstElement);
//
//            return new LzInt(integerList(resultIteration));
//        };
//    }
//}
