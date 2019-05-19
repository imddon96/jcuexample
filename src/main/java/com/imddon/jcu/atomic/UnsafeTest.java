package com.imddon.jcu.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class UnsafeTest {

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {
//        Unsafe unsafe = getUnsafe();
//        System.out.println(Unsafe.class.getClassLoader());
//        System.out.println(UnsafeTest.class.getClassLoader());
//        System.out.println(UnsafeTest.class.getClassLoader().getParent());
//        System.out.println(UnsafeTest.class.getClassLoader().getParent().getParent());
//        System.out.println(unsafe);

        /**
         * StupidCounter
         *  Counter result 6953264
         *  Time passed in ms 373
         *
         * SyncCounter
         *  Counter result 10000000
         *  Time passed in ms 680
         *
         * LockCounter
         *  Counter result 10000000
         *  Time passed in ms 1172
         *
         * AtomicCounter
         *  Counter result 10000000
         *  Time passed in ms 558
         *
         * CasCounter
         *  Counter result 10000000
         *  Time passed in ms 515
         */

        ExecutorService service = Executors.newFixedThreadPool(1000);
        Counter counter = new CasCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter,10000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("Counter result " + counter.getCounter());
        System.out.println("Time passed in ms " + (end-start));


    }

/*    public void publicMethod() {
        System.out.println("In public modifier method");
        new InnerClass().testForMethodInOuterClass();
    }
    private void privateMethod() {
        System.out.println("In private modifier method");

    }

    public class InnerClass {

        public InnerClass() {

        }

        private void testForMethodInOuterClass() {
            privateMethod();
        }
    }*/


    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    interface Counter {
        void increment();

        Long getCounter();
    }

    /**
     * StupidCounter
     */

    static class StupidCounter implements Counter {

        private volatile Long counter = 0L;

        public void increment() {
            counter++;

        }

        public Long getCounter() {
            return counter;
        }
    }

    /**
     * SyncCounter
     */
    static class SyncCounter implements Counter {

        private volatile Long counter = 0L;

        public synchronized void increment() {
            counter++;
        }

        public Long getCounter() {
            return counter;
        }
    }

    /**
     * LockCounter
     */
    static class LockCounter implements Counter {

        private volatile Long counter = 0L;
        private final ReentrantLock lock = new ReentrantLock();

        public synchronized void increment() {
            try {
                lock.lock();
                counter++;
            } finally {
                lock.unlock();
            }
        }

        public Long getCounter() {
            return counter;
        }
    }

    /**
     * AtomicCounter
     */

    static class AtomicCounter implements Counter {

        private AtomicLong counter = new AtomicLong();

        public synchronized void increment() {
            counter.getAndIncrement();
        }

        public Long getCounter() {
            return counter.get();
        }
    }

    /**
     * CasCounter
     */
    static class CasCounter implements Counter {
        private volatile long counter = 0;
        private Unsafe unsafe;
        private long offset;

        public CasCounter() throws NoSuchFieldException {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }

        public synchronized void increment() {
            long current = counter;
            while (!unsafe.compareAndSwapLong(this,offset,current,current+1));
                current = counter;
        }

        public Long getCounter() {
            return counter;
        }
    }


    static class CounterRunnable implements Runnable {

        private final Counter counter;
        private final int num;

        public CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }

}
