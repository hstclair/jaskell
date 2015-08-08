package com.hstclair.jaskell.fifth.demo;

import com.hstclair.jaskell.fifth.function.Expression;

import java.math.BigInteger;

/**
 * @author hstclair
 * @since 7/4/15 7:15 PM
 */
public class FibTuple {

    final Expression<BigInteger> fst;
    final Expression<BigInteger> snd;

    FibTuple(BigInteger fst, BigInteger snd) {
        this.fst = () -> fst;
        this.snd = () -> snd;
    }

    FibTuple(Expression<BigInteger> fst, Expression<BigInteger> snd) {
        this.fst = fst;
        this.snd = snd;
    }
}
