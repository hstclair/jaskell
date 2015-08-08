package com.hstclair.jaskell.fourth.demo;

import com.hstclair.jaskell.fourth.Tuple;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 7/4/15 1:41 PM
 */
public class FourthDemo {

    static final Tuple SEED = new Tuple(BigInteger.ZERO, BigInteger.ONE);

    public static Tuple seedSupplier() {
        return SEED;
    }


    public Tuple fibSum(Supplier<Tuple> fnTupleSupplier, Supplier<Tuple> fkTupleSupplier) {

        Tuple fnTuple = fnTupleSupplier.get();
        Tuple fkTuple = fkTupleSupplier.get();

        BigInteger fnminus1 = (BigInteger) fnTuple.fst().get();
        BigInteger fn = (BigInteger) fnTuple.snd().get();

        BigInteger fkminus1 = (BigInteger) fkTuple.fst().get();
        BigInteger fk = (BigInteger) fkTuple.snd().get();

        return new Tuple(fnminus1.multiply(fkminus1).add(fn.multiply(fk)), fnminus1.multiply(fk).add(fn.multiply(fkminus1.add(fk))));
    }

    public Supplier<Tuple> fibSumSupplier(Supplier<Tuple> tupleSupplier, Supplier<Tuple> accumulatorSupplier) {
        return () -> fibSum(tupleSupplier, accumulatorSupplier);
    }

    public Supplier<Tuple> nextFibSupplier(Supplier<Tuple> tupleSupplier) {
        return () -> fibSum(tupleSupplier, tupleSupplier);
    }

    public Supplier<Tuple> fibGenerator(Supplier<Long> nSupplier, Supplier<Tuple> tupleSupplier, Supplier<Tuple> accumulatorSupplier) {

        long n = nSupplier.get();

        if (n == 0)
            return accumulatorSupplier;

        if ((n & 1l) == 1)
            return fibGenerator(() -> n >> 1, nextFibSupplier(tupleSupplier), fibSumSupplier(tupleSupplier, accumulatorSupplier));

        return fibGenerator(() -> n >> 1, nextFibSupplier(tupleSupplier), accumulatorSupplier);
    }

    public Supplier<BigInteger> fib(long n) {

        Supplier<Tuple> resultSupplier = fibGenerator(() -> n - 1, FourthDemo::seedSupplier, FourthDemo::seedSupplier);

        return () -> (BigInteger) resultSupplier.get().snd().get();
    }
}
