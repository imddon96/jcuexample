package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import static org.junit.Assert.assertEquals;

public class FailedAtomicIntegerFieldUpdater {

    private volatile int i;

    @Test
    public void test1() {
        AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest.TestMe> updater =
                AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterTest.TestMe.class,"i");

        AtomicIntegerFieldUpdaterTest.TestMe me = new AtomicIntegerFieldUpdaterTest.TestMe();

        updater.getAndIncrement(me);
        updater.compareAndSet(me,1,2);

        assertEquals(2,updater.get(me));
    }

    @Test
    public void test3() {

        AtomicIntegerFieldUpdater<FailedAtomicIntegerFieldUpdater> updater =
                AtomicIntegerFieldUpdater.newUpdater(FailedAtomicIntegerFieldUpdater.class,"i");

        FailedAtomicIntegerFieldUpdater me = new FailedAtomicIntegerFieldUpdater();

        updater.getAndIncrement(me);
        updater.compareAndSet(me,1,2);

        assertEquals(2,updater.get(me));

    }

    @Test(expected = RuntimeException.class)
    public void test2() {

        AtomicIntegerFieldUpdater<TestMe2> updater =
                AtomicIntegerFieldUpdater.newUpdater(TestMe2.class,"i");
        TestMe2 me = new TestMe2();

        updater.getAndIncrement(me);
        updater.compareAndSet(me,1,2);

        assertEquals(2,updater.get(me));

    }

    static class TestMe2 {
        volatile int i;
    }

}
