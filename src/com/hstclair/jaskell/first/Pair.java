package com.hstclair.jaskell.first;

/**
 * @author hstclair
 * @since 6/15/15 8:50 PM
 */
public class Pair<A> {
    public final A first;
    public final A second;

    public Pair(A first, A last) {
        this.first = first;
        this.second = last;
    }

    public Pair<A> swap() {
        return new Pair<A>(second, first);
    }
}
