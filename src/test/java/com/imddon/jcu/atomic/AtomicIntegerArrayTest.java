package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.junit.Assert.assertEquals;

public class AtomicIntegerArrayTest {
    @Test
    public void createAtomicIntegerArray() {
        AtomicIntegerArray array = new AtomicIntegerArray(10);
        assertEquals(10,array.length());
    }

    @Test
    public void testSet() {
        AtomicIntegerArray array = new AtomicIntegerArray(10);

        array.set(5,1);

        assertEquals(1,array.get(5));
    }

    @Test
    public void testGetAndSet() {
        AtomicIntegerArray array = new AtomicIntegerArray(10);

        array.getAndSet(1,2);

        assertEquals(2,array.get(1));
    }
}
