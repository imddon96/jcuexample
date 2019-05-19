package com.imddon.jcu.atomic;


import java.sql.Time;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决ABA问题引入版本控制Stamped
 */
public class AtomicStampedReferenceTest {

    private static AtomicStampedReference<Integer> atomicStampedReference =
            new AtomicStampedReference(100, 0);


    public static void main(final String[] args) throws InterruptedException {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    boolean success = atomicStampedReference.compareAndSet(100, 101,
                            atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                    System.out.println(success);

                    success = atomicStampedReference.compareAndSet(101, 100,
                            atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                    System.out.println(success);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    int stamp = atomicStampedReference.getStamp();
                    System.out.println("Before sleep: the stamp is " + stamp);
                    Thread.sleep(500);
                    System.out.println("After sleep: the stamp is " + atomicStampedReference.getStamp());
                    boolean success = atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);
                    System.out.println(success);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
