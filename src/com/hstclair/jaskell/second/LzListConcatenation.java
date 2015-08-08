//package com.hstclair.jaskell.second;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.function.IntConsumer;
//
///**
// * @author hstclair
// * @since 6/20/15 10:14 AM
// */
//public class LzListConcatenation<A> implements LzList<A> {
//
//    class Counter implements IntConsumer {
//
//        int adjustedOffset = 0;
//        int index = 0;
//        boolean done = false;
//
//        public Counter(int offset) {
//            adjustedOffset = offset;
//        }
//
//        @Override
//        public void accept(int value) {
//            if (! done) {
//                if (adjustedOffset - value >= 0) {
//                    adjustedOffset -= value;
//
//                    index++;
//                } else
//                    done = true;
//            }
//        }
//
//        public int getIndex() {
//            return index;
//        }
//
//        public int getAdjustedOffset() {
//            return adjustedOffset;
//        }
//    };
//
//    private enum Internal { INTERNAL };
//
//    private static Internal INTERNAL = Internal.INTERNAL;
//
//    final LzList<A>[] lists;
//
//    final int offset;
//
//    final int size;
//
//    LzListConcatenation(LzList<A>[] lists) {
//        this(INTERNAL, lists, 0, null);
//    }
//
//    LzListConcatenation(LzList<A>[] lists, int offset) {
//        this(INTERNAL, lists, offset, null);
//    }
//
//    LzListConcatenation(LzList<A>[] lists, int offset, int size) {
//        this(INTERNAL, lists, offset, size);
//    }
//
//    LzListConcatenation(Internal internal, LzList<A>[] lists, int offset, Integer size) {
//        LzList<A>[] adjustedLists;
//
//        if (offset > 0) {
//            Counter counter = getStartingListIndex(lists, offset);
//
//
//        }
//    }
//
//    private Counter getStartingListIndex(LzList<A>[] lists, int offset) {
//
//        Counter counter = new Counter(offset);
//
//        Arrays.asList(lists).stream().mapToInt(it -> it.length()).forEach(counter);
//
//        return counter;
//    }
//
//    private static <A> int length(LzList<A>[] lists) {
//        return Arrays.asList(lists).stream().mapToInt(it->it.length()).sum();
//    }
//
//    @Override
//    public boolean isFinite() {
//        return false;
//    }
//
//    @Override
//    public boolean isNull() {
//        return false;
//    }
//
//    @Override
//    public Integer length() {
//        return null;
//    }
//
//    @Override
//    public A head() {
//        return null;
//    }
//
//    @Override
//    public A last() {
//        return null;
//    }
//
//    @Override
//    public LzList<A> init() {
//        return null;
//    }
//
//    @Override
//    public LzList<A> tail() {
//        return null;
//    }
//
//    @Override
//    public LzList<A> concatenate(LzList<A> list) {
//        return null;
//    }
//}
