package com.hstclair.jaskell.third;

import com.hstclair.jaskell.util.CachingSupplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 6/21/15 6:55 PM
 */
public class LzList<A> extends CachingSupplier<LzListInner<A>> {

    volatile Supplier<Boolean> isNullSupplier;

    volatile Supplier<Boolean> isFiniteSupplier;

    volatile Supplier<Integer> lengthSupplier;

    volatile Supplier<A> headSupplier;

    volatile LzList<A> tailSupplier;

    volatile Supplier<A> lastSupplier;

    volatile LzList<A> initSupplier;

    volatile LzList<A> takeSupplier;

    volatile LzList<A> dropSupplier;

    volatile LzList<A> concatenateSupplier;

    public LzList(Supplier<LzListInner<A>> listSupplier) {
        super(listSupplier);
    }

    public LzList(A[] array) {
        this(() -> { return new LzListInnerImpl<A>(array); });
    }

    public LzList(LzSupplierBuilder<A> supplierBuilder) {
        this(() -> { return new LzListInnerImpl<A>(supplierBuilder); });
    }

    public static <A> Function<LzList<A>, Boolean> isFinite() {
        return (list) -> list.isFiniteApply().get();
    }

    public static <A> Function<LzList<A>, Boolean> isNull() {
        return (list) -> list.isNullApply().get();
    }

    public static <A> Function<LzList<A>, Integer> length() {
        return (list) -> list.lengthApply().get();
    }

    public static <A> Function<LzList<A>, A> head() {
        return (list) -> list.headApply().get();
    }

    public static <A> Function<LzList<A>, A> last() {
        return (list) -> list.lastApply().get();
    }

    public static <A> Function<LzList<A>, LzList<A>> init() {
        return LzList::initApply;
    }

    public static <A> Function<LzList<A>, LzList<A>> tail() {
        return LzList::tailApply;
    }

    public static <A> BiFunction<LzList<A>, Integer, LzList<A>> take() {
        return LzList::takeApply;
    }

    public static <A> BiFunction<LzList<A>, Integer, LzList<A>> drop() {
        return LzList::dropApply;
    }

    public static <A> BiFunction<LzList<A>, LzList<A>, LzList<A>> concatenate() {
        return LzList::concatenateApply;
    }

    private Supplier<Boolean> isFiniteApply() {
        if (isFiniteSupplier == null) {
            synchronized (this) {
                if (isFiniteSupplier == null)
                    isFiniteSupplier = new CachingSupplier<>(() -> { return get().isFinite(); });
            }
        }
        return isFiniteSupplier;
    }

    private Supplier<Boolean> isNullApply() {
        if (isNullSupplier == null) {
            synchronized (this) {
                if (isNullSupplier == null)
                    isNullSupplier = new CachingSupplier<>(() -> { return get().isNull(); });
            }
        }

        return isNullSupplier;
    }

    private Supplier<Integer> lengthApply() {
        if (lengthSupplier == null) {
            synchronized (this) {
                if (lengthSupplier == null)
                    lengthSupplier = new CachingSupplier<>(() -> { return get().length(); });
            }
        }

        return lengthSupplier;
    }

    private Supplier<A> headApply() {
        if (headSupplier == null) {
            synchronized (this) {
                if (headSupplier == null)
                    headSupplier = new CachingSupplier<>(() -> { return get().head(); });
            }
        }

        return headSupplier;
    }

    private Supplier<A> lastApply() {
        if (lastSupplier == null) {
            synchronized (this) {
                if (lastSupplier == null)
                    lastSupplier = new CachingSupplier<>(() -> get().last());
            }
        }

        return lastSupplier;
    }

    private LzList<A> initApply() {
        if (initSupplier == null) {
            synchronized (this) {
                if (initSupplier == null)
                    initSupplier = new LzList<A>(() -> { return get().init(); });
            }
        }

        return initSupplier;
    }

    private LzList<A> tailApply() {
        if (tailSupplier == null) {
            synchronized (this) {
                if (tailSupplier == null)
                    tailSupplier = new LzList<A>(() -> { return get().tail(); });
            }
        }

        return tailSupplier;
    }

    private LzList<A> takeApply(int length) {
        if (takeSupplier == null) {
            synchronized (this) {
                if (takeSupplier == null)
                    takeSupplier = new LzList<A>(() -> { return get().take(length); });
            }
        }

        return takeSupplier;
    }

    private LzList<A> dropApply(int length) {
        if (dropSupplier == null) {
            synchronized (this) {
                if (dropSupplier == null)
                    dropSupplier = new LzList<A>(() -> { return get().drop(length); });
            }
        }

        return dropSupplier;
    }

    private LzList<A> concatenateApply(LzList<A> list) {
        if (concatenateSupplier == null) {
            synchronized (this) {
                if (concatenateSupplier == null)
                    concatenateSupplier = new LzList<A>(() -> { return get().concatenate(list.get()); });
            }
        }

        return concatenateSupplier;
    }
}
