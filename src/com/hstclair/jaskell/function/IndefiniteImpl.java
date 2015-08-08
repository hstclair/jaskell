package com.hstclair.jaskell.function;


import java.util.List;

/**
 * @author hstclair
 * @since 7/4/15 2:23 PM
 */
public class IndefiniteImpl<T> implements Indefinite<T> {

    final Expression<T> resultExpression;

    final Expression<Boolean> isPresentExpression;

    protected IndefiniteImpl() {
        resultExpression = IndefiniteImpl::noResult;
        isPresentExpression = () -> false;
    }

    public <V> IndefiniteImpl(Indefinite<V> indefinite, Function<? super V, ? extends T> transformation) {
        if (indefinite instanceof IndefiniteImpl) {
            this.resultExpression = ((IndefiniteImpl<V>) indefinite).resultExpression.andThen(transformation);
            this.isPresentExpression = ((IndefiniteImpl<V>) indefinite).isPresentExpression;
        } else {
            this.resultExpression = Expression.of(indefinite::evaluate).andThen(transformation);
            this.isPresentExpression = Expression.of(indefinite::isPresent);
        }
    }

    public IndefiniteImpl(Expression<T> resultExpression, Expression<Boolean> isPresentExpression) {
        this.resultExpression = resultExpression;
        this.isPresentExpression = isPresentExpression;
    }

    private static <T> T noResult() {
        throw new IllegalStateException("Expression is empty");
    }

    private static <T> T orElseGet(Indefinite<T> expression, Indefinite<T> alternateExpression) {
        if (expression.isPresent())
            return expression.evaluate();

        return alternateExpression.evaluate();
    }

    private static Boolean isPresentAggregate(Expression<Boolean> primaryIsPresent, Expression<Boolean> secondaryIsPresent) {
        return primaryIsPresent.evaluate() || secondaryIsPresent.evaluate();
    }

    public static <T> Indefinite<T> orElseImpl(Indefinite<T> primarySupplier, Indefinite<T> alternateSupplier) {
        Expression<T> expression = () -> orElseGet(primarySupplier, alternateSupplier);
        Expression<Boolean> isPresent = () -> isPresentAggregate(primarySupplier.getIsPresentExpression(), alternateSupplier.getIsPresentExpression());

        return new IndefiniteImpl<>(expression, isPresent);
    }

    @Override
    public Expression getExpression() {
        return resultExpression.getExpression();
    }

    @Override
    public List<Function> getTransformations() { return resultExpression.getTransformations(); }

    @Override
    public Expression<Boolean> getIsPresentExpression() { return isPresentExpression; }

    @Override
    public T evaluate() {
        return resultExpression.evaluate();
    }

    @Override
    public Boolean isPresent() {
        return isPresentExpression.evaluate();
    }

    @Override
    public <R> Indefinite<R> andThen(Function<? super T, ? extends R> transformation) {
        return new IndefiniteImpl<>(resultExpression.andThen(transformation), isPresentExpression);
    }
}
