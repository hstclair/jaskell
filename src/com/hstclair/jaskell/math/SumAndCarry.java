//package com.hstclair.jaskell.math;
//
//import com.hstclair.jaskell.function.Expression;
//import com.hstclair.jaskell.data.LzList;
//import com.hstclair.jaskell.function.Indefinite;
//import com.hstclair.jaskell.function.IndefiniteImpl;
//
//
///**
// * @author hstclair
// * @since 7/9/15 9:02 PM
// */
//public class SumAndCarry {
//
//    final Indefinite<Integer> sumSupplier;
//
//    final Expression<Long> carrySupplier;
//
//    final int nextIndex;
//
//    public SumAndCarry(SumAndCarry previous, LzList<Integer> addendL, LzList<Integer> addendR) {
//        this(previous.nextIndex, addendL.get(previous.nextIndex), addendR.get(previous.nextIndex), previous.carrySupplier);
//    }
//
//    public SumAndCarry(int index, Indefinite<Integer> a, Indefinite<Integer> b, Expression<Long> carrySupplier) {
//        this.nextIndex = index + 1;
//
//        Indefinite<Long> longSumWithCarrySupplier = Indefinite.unwrap(Indefinite.of(() -> sumOfOptionalIntegers(a, b))).andThen((value) -> value + carrySupplier.evaluate());
//
//        // TODO: Does this end properly when we run out of digits???
//
//        this.sumSupplier = longSumWithCarrySupplier.andThen(Long::intValue).cache();
//        this.carrySupplier = ((Expression<Long>) (() -> noPossibleCarry(a, b) ? 0l : computedCarry(longSumWithCarrySupplier))).cache();
//    }
//
//    public int getSum() {
//        return sumSupplier.evaluate();
//    }
//
//    static Indefinite<Long> sumOfOptionalIntegers(Indefinite<Integer> a, Indefinite<Integer> b) {
//
//        Expression<Boolean> isPresentSupplier = () -> a.isPresent() || b.isPresent();
//        Expression<Expression<Long>> expression = () -> {
//            if (! b.isPresent()) return a.andThen(Integer::longValue);
//            if (! a.isPresent()) return b.andThen(Integer::longValue);
//
//            return Indefinite.of(a.evaluate().longValue() + b.evaluate().longValue());
//        };
//
//        return new IndefiniteImpl<>(expression.andThen(Expression::evaluate), isPresentSupplier);
//    }
//
//    static boolean noPossibleCarry(Indefinite<Integer> a, Indefinite<Integer> b) {
//        boolean aPresent = a.isPresent();
//        boolean bPresent = b.isPresent();
//
//        if (! (aPresent || bPresent)) return true;
//
//        return (! (aPresent && bPresent));
//    }
//
//    static int computedCarry(Expression<Long> sumWithCarry) {
//        return (sumWithCarry.evaluate() & 0x100000000l) != 0 ? 1 : 0;
//    }
//}
//
