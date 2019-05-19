package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtomicBooleanTest {

    @Test
    public void  testCreate() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        assertFalse(atomicBoolean.get());
    }

    @Test
    public void testCreateWithArgument() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        assertTrue(atomicBoolean.get());
    }

    @Test
    public void testGetAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        atomicBoolean.getAndSet(false);
        assertFalse(atomicBoolean.get());
    }

    @Test
    public void testCompareAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        boolean result = atomicBoolean.compareAndSet(false,true);

        assertFalse(result);

        assertTrue(atomicBoolean.get());
    }

}
