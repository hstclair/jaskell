package com.hstclair.jaskell.function;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * @author hstclair
 * @since 8/8/15 9:19 PM
 */
public class TestComposable {

    void pass() {}

    @Test
    public void testComposeWithInputTransformerRejectsNull() {
        Composable<Object, Object> instance = o -> null;

        try {
            instance.composeWithInputTransformer(null);
            fail();
        } catch (NullPointerException npe) { pass(); }
    }

    @Test
    public void testComposeReturnsOperationList() {
        Composable<Object, Object> instance = o -> null;

        Operation<Object, Object> composed = instance.composeWithInputTransformer(o -> null);

        assertEquals(OperationList.class, composed.getClass());
    }

}
