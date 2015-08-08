package com.hstclair.jaskell.fifth.function;

import com.hstclair.jaskell.fifth.collections.FunctionalIterable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 7/4/15 3:10 PM
 */
public class ExpressionImpl<T> implements Expression<T> {

    final Expression expression;

    final List<Function> transformations;

    ExpressionImpl(Expression<T> expression) {
        this(expression, Collections.EMPTY_LIST);
    }

    <V> ExpressionImpl(Expression<? extends V> expression, Function<? super V, ? extends T> transformation) {
        this(expression, Collections.singletonList(transformation));
    }

    ExpressionImpl(Expression expression, List<Function> transformations) {
        if (expression.getClass() == ExpressionImpl.class || expression.getClass() == SubstituteExpressionImpl.class) {
            this.expression = expression.getExpression();
            LinkedList<Function> transformationList = new LinkedList<Function>(expression.getTransformations());
            transformationList.addAll(transformations);
            this.transformations = Collections.unmodifiableList(transformationList);
        } else {
            this.expression = expression;
            this.transformations = transformations;
        }
    }

    @Override
    public <R> Expression<R> andThen(Function<? super T, ? extends R> transformation) {
        List<Function> newTransformations = new LinkedList<>(transformations);
        newTransformations.add(transformation);

        return new ExpressionImpl<>(expression, Collections.unmodifiableList(newTransformations));
    }

    static Expression findStartingExpression(Expression expression, LinkedList<Function> allTransformations) {
        Expression startingExpression = expression;
//        final Expression[] startingExpression = { expression };

        // TODO: More Functional

//        FunctionalIterable<Expression> functionalIterable = FunctionalIterable.of(expression, Expression::getExpression);

//        functionalIterable.takeUntil(it -> { startingExpression[0] = it; return it.getClass() != ExpressionImpl.class; })
//                .forEach(it -> allTransformations.addAll(it.getTransformations()));

        while (startingExpression instanceof ExpressionImpl) {
            allTransformations.addAll(startingExpression.getTransformations());
            startingExpression = startingExpression.getExpression();
        }

        return startingExpression;
    }

    @Override
    public T evaluate() {
        Object result;

        LinkedList<Function> allTransformations = new LinkedList<>();

        // unpack upstream substitutions until we have a starting expression and a list of all transformations to apply
        Expression startingExpression = findStartingExpression(this, allTransformations);

        result = startingExpression.evaluate();

        for (Function transformation : allTransformations)
            result = transformation.apply(result);

        return (T) result;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public List<Function> getTransformations() {
        return transformations;
    }
}
