package com.hstclair.jaskell.function;

import java.util.Objects;

/**
 * @author hstclair
 * @since 7/4/15 5:16 PM
 */
@FunctionalInterface
public interface Indefinite<T> extends Expression<T> {
    Indefinite EMPTY = new IndefiniteImpl<>();

    default Boolean isPresent() { return true; }

    @Override
    T evaluate();

    static <T> Indefinite<T> of(T value) {
        Objects.requireNonNull(value);

        return () -> value;
    }

    static <T> Indefinite<Indefinite<T>> of(Indefinite<T> indefinite) {
        Objects.requireNonNull(indefinite);

        return () -> indefinite;
    }

    static <T> Indefinite<T> of(T value, Expression<Boolean> predicate) {
        return new IndefiniteImpl<>(() -> value, predicate);
    }

    static <T> Indefinite<T> of(Expression<T> valueExpression) {
        Objects.requireNonNull(valueExpression);

        return valueExpression::evaluate;
    }

    static <T> Indefinite<T> of(Expression<T> valueExpression, Expression<Boolean> isPresentExpression) {
        Objects.requireNonNull(valueExpression);
        Objects.requireNonNull(isPresentExpression);

        return new IndefiniteImpl<>(valueExpression, isPresentExpression);
    }

    default <R> Indefinite<R> andThen(Function<T, R> transformation) {
        Objects.requireNonNull(transformation);

        return new IndefiniteImpl<>(((Expression<T>) this::evaluate).andThen(transformation), alwaysTrue);
    }

    // TODO: Probably need a Selector class
    default Expression<T> orElse(T alternate) {
        Selector<Boolean, Expression<T>> selector = new Selector<>((Expression<T>) this::evaluate, (Expression<T>) ()->alternate);

        Expression<Boolean> predicate = this::isPresent;

        Expression<Expression<T>> expression = predicate.andThen(selector::performOperation).andThen(OperationExpressionizer::new);

        // for some reason I can't quite follow at the moment, Java seems to reject expression::evaluate, which I believe should apply to Expression<T>

        return () -> expression.evaluate().evaluate();
    }

    default Expression<Indefinite<T>> orElse(Indefinite<T> indefinite) {
        Selector<Boolean, Expression<Indefinite<T>>> selector = new Selector<>((Expression<Indefinite<T>>) () -> this, (Expression<Indefinite<T>>) ()->indefinite);

        Expression<Boolean> predicate = this::isPresent;

        Expression<Expression<Indefinite<T>>> expression = new OperationExpressionizer<>(predicate.andThen(selector::performOperation));

        // for some reason I can't quite follow at the moment, Java seems to reject expression::apply, which I believe should apply to Expression<T>

        return () -> expression.evaluate().evaluate();
    }

    default Expression<T> orElse(Expression<T> alternate) {
        return () -> isPresent() ? evaluate() : alternate.evaluate();
    }

    default Expression<Boolean> getIsPresentExpression() { return alwaysTrue; }

    default Expression<T> getEvaluateExpression() { return this::evaluate; }

    default Indefinite<T> cache() {
        Expression<T> getter = this::evaluate;
        Expression<Boolean> present = this::isPresent;

        return new IndefiniteImpl<>(getter.cache(), present.cache());
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

        return new IndefiniteImpl<>(() -> extractExpression(indefinite, isPresentExpr), isPresentExpr);
    }

    static <T> Indefinite<T> unwrap(Expression<Indefinite<T>> optionalSupplier) {
        return new IndefiniteImpl<>(() -> optionalSupplier.evaluate().evaluate(), () -> optionalSupplier.evaluate().isPresent());
    }
}
