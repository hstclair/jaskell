package com.hstclair.jaskell.third;

/**
 * @author hstclair
 * @since 6/20/15 4:04 PM
 */
public interface LzListInner<A> {

    public static final LzListInner EMPTY = LzEmptyInner.EMPTY;

    boolean isFinite();

    boolean isNull();

    Integer length();

    A head();

    A last();

    LzListInner<A> init();

    LzListInner<A> tail();

    LzListInner<A> take(int length);

    LzListInner<A> drop(int length);

    LzListInner<A> concatenate(LzListInner<A> list);
}
