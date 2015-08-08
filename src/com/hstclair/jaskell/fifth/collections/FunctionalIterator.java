package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.function.Consumer;
import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Function;

/** Extension of Iterator interface to introduce generator methods (e.g. eachUntil)
 *
 * @author hstclair
 * @since 7/18/15 2:32 PM
 */
public interface FunctionalIterator<T> extends java.util.Iterator<T> {


    @Override
    default void forEachRemaining(java.util.function.Consumer<? super T> action) {
        forEachRemaining(Consumer.of(action));
    }

//  TODO : Implement other methods (requires other Strategies)
//    default FunctionalIterator<T> filter(Function<T, Boolean> untilPredicate) {
//    }

    /**
     * Construct a FunctionalIterator that will permit the consuming entity to access each item in a collection until
     * the collection has been exhausted or until the untilPredicate condition has been satisfied.
     *
     * @param predicate a Function that accepts a member of the iteration, <i>before</i> it is presented to the consumer,
     *                  and returns true of the iteration is complete or false if the iteration may continue.
     * @return
     */
    default FunctionalIterator<T> eachUntil(Function<T, Boolean> predicate) {
        return new FunctionalIteratorImpl<T>(new FunctionalIteratorUntilStrategy<>(this::next, this::hasNext, predicate));
    }

    default void forEachRemaining(Consumer<? super T> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }
}
