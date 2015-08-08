package com.hstclair.jaskell.second;

/**
 * @author hstclair
 * @since 6/20/15 9:32 AM
 */
public interface LzList<A> {

    public static final LzList EMPTY = new LzEmpty();

    boolean isFinite();

    boolean isNull();

    Integer length();

    A head();

    A last();

    LzList<A> init();

    LzList<A> tail();

    LzList<A> take(int length);

    LzList<A> drop(int length);

    LzList<A> concatenate(LzList<A> list);
}
