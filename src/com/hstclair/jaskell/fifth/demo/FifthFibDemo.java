package com.hstclair.jaskell.fifth.demo;

import com.hstclair.jaskell.fifth.function.Expression;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 7/4/15 1:44 PM
 */
public class FifthFibDemo {

    static final FibTuple SEED = new FibTuple(BigInteger.ZERO, BigInteger.ONE);

    public static FibTuple seedSupplier() {
        return SEED;
    }


    public FibTuple fibSum(Expression<FibTuple> fnTupleSupplier, Expression<FibTuple> fkTupleSupplier) {

        FibTuple fnTuple = fnTupleSupplier.evaluate();
        FibTuple fkTuple = fkTupleSupplier.evaluate();

        BigInteger fnminus1 = fnTuple.fst.evaluate();
        BigInteger fn = fnTuple.snd.evaluate();

        BigInteger fkminus1 = fkTuple.fst.evaluate();
        BigInteger fk = fkTuple.snd.evaluate();

        return new FibTuple(fnminus1.multiply(fkminus1).add(fn.multiply(fk)), fnminus1.multiply(fk).add(fn.multiply(fkminus1.add(fk))));
    }

    public Expression<FibTuple> fibSumSupplier(Expression<FibTuple> tupleSupplier, Expression<FibTuple> accumulatorSupplier) {
        return () -> fibSum(tupleSupplier, accumulatorSupplier);
    }

    public Expression<FibTuple> nextFibSupplier(Expression<FibTuple> tupleSupplier) {
        return () -> fibSum(tupleSupplier, tupleSupplier);
    }

    public Expression<FibTuple> fibGenerator(Expression<Long> nSupplier, Expression<FibTuple> tupleSupplier, Expression<FibTuple> accumulatorSupplier) {

        long n = nSupplier.evaluate();

        if (n == 0)
            return accumulatorSupplier;

        if ((n & 1l) == 1)
            return fibGenerator(() -> n >> 1, nextFibSupplier(tupleSupplier), fibSumSupplier(tupleSupplier, accumulatorSupplier));

        return fibGenerator(() -> n >> 1, nextFibSupplier(tupleSupplier), accumulatorSupplier);
    }

    public Expression<BigInteger> fib(long n) {

        Expression<FibTuple> resultSupplier = fibGenerator(() -> n - 1, FifthFibDemo::seedSupplier, FifthFibDemo::seedSupplier);

        return resultSupplier.evaluate().snd;
    }

}
