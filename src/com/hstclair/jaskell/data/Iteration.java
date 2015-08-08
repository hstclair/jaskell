package com.hstclair.jaskell.data;

import com.hstclair.jaskell.function.Function;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hstclair
 * @since 7/6/15 11:30 PM
 */
class Iteration<T> implements Function<Integer, T> {
    volatile int elementCount;

    final List<T> elements;

    final Function<T, T> generator;

    Iteration(Function<T, T> generator, T seed) {
        elementCount = 1;
        elements = new LinkedList<>();
        this.generator = generator;
        elements.add(seed);
    }

    public T get(int index) {
        if (elementCount <= index) {
            synchronized (this) {
                if (elementCount <= index) {
                    for (int count = elementCount; count <= index; count++) {
                        elements.add(generator.apply(elements.get(count - 1)));
                        elementCount++;
                    }
                }
            }
        }

        return elements.get(index);
    }

    @Override
    public T apply(Integer index) {
        return get(index);
    }
}
