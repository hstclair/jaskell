package com.hstclair.jaskell.function;


/**
 * @author hstclair
 * @since 7/4/15 2:23 PM
 */
public class IndefiniteImpl<T> implements Indefinite<T> {

    final Expression<T> evaluateExpression;

    final Expression<Boolean> isPresentExpression;

    protected IndefiniteImpl() {
        evaluateExpression = IndefiniteImpl::noResult;
        isPresentExpression = () -> false;
    }

    public IndefiniteImpl(Expression<T> evaluateExpression, Expression<Boolean> isPresentExpression) {
        this.evaluateExpression = evaluateExpression;
        this.isPresentExpression = isPresentExpression;
    }

    @Override
    public Expression<Boolean> getIsPresentExpression() {
        return isPresentExpression;
    }

    @Override
    public Expression<T> getEvaluateExpression() {
        return evaluateExpression;
    }

    private static <T> T noResult() {
        throw new IllegalStateException("Expression is empty");
    }

    @Override
    public T evaluate() {
        Evaluator<?, T> evaluator = new Evaluator<>();
        return evaluator.evaluate(evaluateExpression);
    }

    @Override
    public Boolean isPresent() {
        Evaluator<?, Boolean> evaluator = new Evaluator<>();
        return evaluator.evaluate(isPresentExpression);
    }

    @Override
    public <R> Indefinite<R> andThen(Function<T, R> transformation) {
        return new IndefiniteImpl<>(evaluateExpression.andThen(transformation), isPresentExpression);
    }
}
