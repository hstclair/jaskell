package com.hstclair.jaskell.first;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/15/15 9:36 PM
 */
public class FiniteLzList<T> extends LzList {

    private enum SPECIAL {
        EMPTYLIST,
        TAIL
    }

    public static FiniteLzList EMPTY_LIST = new FiniteLzList(SPECIAL.EMPTYLIST);

//    final List<LzListImpl<T>> tails;

    final int size;

    private FiniteLzList(SPECIAL emptylist) {
        super(null);
        size = 0;
    }

    public FiniteLzList(LzIterator<Supplier<T>> supplier, int size) {
        super(supplier);
        this.size = size;
    }

    public FiniteLzList(T head, FiniteLzList<T> tail) {
        this.head = head;
        this.tails = Collections.singletonList(tail);
    }


    public FiniteLzList(FiniteLzList<T> first, Collection<LzList<T>> lists) {
        head = first.head;

        LinkedList<LzList<T>> newTails = new LinkedList<LzList<T>>(lists);

        newTails.addFirst(first);

        tails = newTails;
    }

    private FiniteLzList(SPECIAL special, Collection<LzList<T>> tails) {
        LinkedList<LzList<T>> newTails = new LinkedList<LzList<T>>(tails);

        LzList<T> firstTail = newTails.getFirst();

        head = firstTail.head();

        newTails.set(0, firstTail.tail());

        this.tails = newTails;
    }

    public FiniteLzList()


    T head() {
        return head;
    }

    FiniteLzList<T> tail() {
        if (tails.size() == 0)
            return EMPTY_LIST;
        if (tails.size() == 1)
            return tails.get(0);
        return new FiniteLzList<T>(SPECIAL.EMPTYLIST, tails);
    }

    static <E, R> Function<FiniteLzList<E>, FiniteLzList<R>> mapper(Function<E, R> mapfunction) {
        return (src) -> { return new FiniteLzList<R>(mapfunction.apply(src.head), mapper(mapfunction).apply(src.tail()) ); };
    }
}
