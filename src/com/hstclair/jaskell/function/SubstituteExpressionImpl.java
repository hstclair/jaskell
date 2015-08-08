package com.hstclair.jaskell.function;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 7/10/15 5:23 PM
 */
public class SubstituteExpressionImpl<T> extends ExpressionImpl<T> {

    final Indefinite<T> substituteSupplier;

    public SubstituteExpressionImpl(Expression<T> expression, Indefinite<T> substituteSupplier) {
        super(expression);

        this.substituteSupplier = substituteSupplier;
    }

    @Override
    public <R> Expression<R> andThen(Function<? super T, ? extends R> transformation) {
        List<Function> newTransformations = new LinkedList<>();
        newTransformations.add(transformation);

        return new ExpressionImpl<>(this, newTransformations);
    }

    @Override
    public Expression getExpression() {
        return () -> substituteSupplier.isPresent() ? substituteSupplier.getExpression().evaluate() : expression.evaluate();
    }

    @Override
    public List<Function> getTransformations() {
        return substituteSupplier.isPresent() ? substituteSupplier.getTransformations(): this.transformations;
    }

}
