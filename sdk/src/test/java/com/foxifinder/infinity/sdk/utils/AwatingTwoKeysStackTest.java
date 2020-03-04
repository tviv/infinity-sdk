package com.foxifinder.infinity.sdk.utils;

import com.foxifinder.infinity.sdk.utils.AwatingTwoKeysStack;

import org.junit.Test;

import static org.junit.Assert.*;

public class AwatingTwoKeysStackTest {

    private AwatingTwoKeysStack<String> stack = new AwatingTwoKeysStack<>();

    @Test
    public void oneOperation__what_pushed_that_get_and_removed_it_then() {
        stack.push("t1", 10, "i10", false);

        assertEquals("i10", stack.pop(10));
        assertNull(stack.pop(10));
    }

    @Test
    public void twoSameTypes__only_last_must_return_and_no_first() {
        stack.push("t1", 10, "i10", false);
        stack.push("t2", 20, "i20", false);
        stack.push("t1", 11, "i11", false);

        assertNull(stack.pop(10));
        assertEquals("i11", stack.pop(11));
    }

    @Test
    public void reusableTest__value_can_be_returned_many_times() {
        stack.push("t1", 11, "i11", true);

        assertEquals("i11", stack.pop(11));
        assertEquals("i11", stack.pop(11));
        assertEquals("i11", stack.pop(11));
    }

}