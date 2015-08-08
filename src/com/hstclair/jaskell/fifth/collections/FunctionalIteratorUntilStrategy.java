package com.hstclair.jaskell.fifth.collections;

import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fifth.function.Function;
import com.hstclair.jaskell.fifth.function.Indefinite;

import java.util.Objects;

/**
 * Implementation of FunctionalIteratorStrategy allowing an Iteration to be bounded by an Until condition.
 * Members of the Iteration may be retrieved up to, but not including, a member that satisfies the Until untilPredicate
 * or until the Iteration is depleted.
 *
 * @author hstclair
 * @since 7/18/15 6:02 PM
 */
public class FunctionalIteratorUntilStrategy<T> extends FunctionalIteratorStrategy<T> {

    /** the untilPredicate function that determines whether the Iteration has been depleted */
    final Function<T, Boolean> untilPredicate;

    /** True if the predicate has been satisfied. */
    boolean predicateSatisfied = false;

    /** the location used to store the current Iteration value (this is required because the hasNextExpression() operation MUST
     *  retrieve the nextExpression member in order to determine whether it satisfies the Until predicate) */
    Indefinite<T> current;

    /**
     * Constructor - constructs a new FunctionalIteratorStrategy by applying an UntilPredicate to a prior instance of
     * FunctionalIteratorStrategy
     *
     * @param strategy an existing FunctionalIteratorStrategy to which an UntilPredicate will be applied
     * @param untilPredicate the UntilPredicate used to limit the existing FunctionalIteratorStrategy
     */
    public FunctionalIteratorUntilStrategy(FunctionalIteratorStrategy<T> strategy, Function<T, Boolean> untilPredicate) {
        this(Objects.requireNonNull(strategy).getNextExpression(), strategy.getHasNextExpression(), untilPredicate);
    }

    /**
     * Constructor - constructs a new FunctionalIteratorStrategy from parts
     *
     * @param nextExpression the nextExpression() expression to be used
     * @param hasNextExpression the hasNextExpression() expression to be used
     * @param untilPredicate the untilPredicate to be used
     */
    public FunctionalIteratorUntilStrategy(Expression<T> nextExpression, Expression<Boolean> hasNextExpression, Function<T, Boolean> untilPredicate) {
        Objects.requireNonNull(nextExpression);
        Objects.requireNonNull(hasNextExpression);
        Objects.requireNonNull(untilPredicate);

        // initialize the current value
        current = Indefinite.EMPTY;

        this.untilPredicate = untilPredicate;

        Expression<Indefinite<T>> currentOrNext = () -> getCurrentOrNext(hasNextExpression, nextExpression);

        // get the current (or nextExpression) value, apply the predicate, and then report whether any Iteration member is present
        this.hasNextExpression = currentOrNext
                .andThen(Indefinite::isPresent);

        // get the current (or nextExpression) value, apply the predicate, and then consume the current value
        this.nextExpression = currentOrNext
                .andThen(this::consumeCurrent);
    }

    /**
     * retrieve either the current value or, if there is no current value, retrieves the nextExpression value.
     *
     * @return the current value, if still available, or the nextExpression value.
     */
    Indefinite<T> getCurrentOrNext(Expression<Boolean> originalHasNextExpression, Expression<T> originalNextExpression) {
        if (current.isPresent()) return current;

        if (predicateSatisfied)
            return current = Indefinite.EMPTY;

        if (! originalHasNextExpression.evaluate())
            return current = Indefinite.EMPTY;

        T nextValue = originalNextExpression.evaluate();

        predicateSatisfied = untilPredicate.apply(nextValue);

        return current = Indefinite.of(nextValue, ! predicateSatisfied);
    }

    /**
     * Consume the current value if it does not satisfy the UntilPredicate
     * Failing to consume the value that satisfies the UntilPredicate causes iteration to halt on that value because
     * no further values will be retrieved
     *
     * @param currentIndefinite the current Indefinite value
     * @return the current iteration value
     */
    T consumeCurrent(Indefinite<T> currentIndefinite) {
        if (! currentIndefinite.isPresent())
            throw new IllegalStateException("Iterator depleted");

        current = Indefinite.EMPTY;

        return currentIndefinite.evaluate();
    }
}
