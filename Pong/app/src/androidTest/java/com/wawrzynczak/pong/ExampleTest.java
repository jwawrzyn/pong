package com.wawrzynczak.pong;

import android.test.InstrumentationTestCase;

/**
 * Created by jenny on 11/18/2014.
 */
public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}

