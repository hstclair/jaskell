package com.hstclair.jaskell.fifth.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 5:16 PM
 */
@FunctionalInterface
public interface Indefinite<T> extends Expression<T> {
    public static final Indefinite EMPTY = new IndefiniteImpl<>();

    default Boolean isPresent() { return true; }

    @Override
    T evaluate();

    static <T> Indefinite<T> of(T value) {
        Objects.requireNonNull(value);

        return () -> value;
    }

    static <T> Indefinite<Indefinite<T>> of(Indefinite<T> indefinite) {
        return new IndefiniteImpl<>(() -> indefinite, () -> true);
    }

    static <T> Indefinite<T> of(T value, Expression<Boolean> predicate) {
        return new IndefiniteImpl<>(() -> value, predicate);
    }

    static <T> Indefinite<T> of(T value, boolean present) {
        if (! present) return Indefinite.EMPTY;
        return Indefinite.of(value);
    }

    static <T> Indefinite<T> of(Expression<T> valueExpression) {
        Objects.requireNonNull(valueExpression);

        if (valueExpression instanceof ExpressionImpl) {
            return new IndefiniteImpl<T>(valueExpression, ()-> true);
        }

        return valueExpression::evaluate;
    }

    static <T> Indefinite<T> of(Expression<T> valueExpression, Expression<Boolean> isPresentExpression) {
        Objects.requireNonNull(valueExpression);
        Objects.requireNonNull(isPresentExpression);

        return new IndefiniteImpl<>(valueExpression, isPresentExpression);
    }

    default <R> Indefinite<R> andThen(Function<? super T, ? extends R> transformation) {
        return new IndefiniteImpl<>(this, transformation);
    }

    default Expression<T> orElse(T alternate) {
        return () -> isPresent() ? evaluate() : alternate;
    }

    default Indefinite<T> orElse(Indefinite<T> alternateIndefinite) {
        return IndefiniteImpl.orElseImpl(this, alternateIndefinite);
    }

    default Expression<T> orElse(Expression<T> alternate) {
        return () -> isPresent() ? evaluate() : alternate.evaluate();
    }

    default Indefinite<T> cache() {

        Expression<T> getter = this::evaluate;
        Expression<Boolean> present = this::isPresent;

        return new IndefiniteImpl<>(getter.cache(), present.cache());
    }

    default Expression<Boolean> getIsPresentExpression() { return this::isPresent; }

    default Indefinite<Indefinite<T>> wrap() {
        return new IndefiniteImpl<>(()->this, this::isPresent);
    }

    static <T> T extractExpression(Indefinite<Indefinite<T>> indefinite, Expression<Boolean> isPresent) {
        if (isPresent.evaluate())
            return indefinite.evaluate().evaluate();
        else
            throw new IllegalStateException("Empty Supplier");
    }

    static <T> Boolean extractIsPresent(Indefinite<Indefinite<T>> indefinite) {
        return indefinite.isPresent() && indefinite.evaluate().isPresent();
    }


    static <T> Indefinite<T> unwrap(Indefinite<Indefinite<T>> indefinite) {
        Expression<Boolean> isPresentExpr = ((Expression<Boolean>) () -> extractIsPresent(indefinite)).cache();

        return new IndefiniteImpl<T>(() -> extractExpression(indefinite, isPresentExpr), isPresentExpr);
    }

    static <T> Indefinite<T> unwrap(Expression<Indefinite<T>> optionalSupplier) {
        return new IndefiniteImpl<>(() -> optionalSupplier.evaluate().evaluate(), () -> optionalSupplier.evaluate().isPresent());
    }
}
