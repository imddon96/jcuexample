package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    // 多次用到的用常量定义到一起
    private static final int MAX_VALUE = 500;
    private static Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>());
    private static volatile int value = 0;


    @Test
    public void test1() throws Exception {

        Thread t1 = new Thread("t1-> ") {
            @Override
            public void run() {
                int x = 0;
                while (x < MAX_VALUE) {
                    set.add(value);
                    int tmp = value;
                    System.out.println(Thread.currentThread().getName() + tmp);
                    value += 1;
                    x++;
                }
            }
        };

        Thread t2 = new Thread("t2-> ") {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    set.add(value);
                    int tmp = value;
                    System.out.println(Thread.currentThread().getName() + tmp);
                    value += 1;
                    /**
                     * value = value + 1
                     * (1) get value from main memory to the local memory
                     * (2) add 1 to => x
                     * (3) assign the value to x
                     * (4) flush x to main memory
                     */
                    x += 1;
                }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(set.size());

    }


    /**
     * AtomicInteger的value用volatile修饰保证了可见行和有序性，
     * unsafe机制保证了原子性
     */
    @Test
    public void test2() throws Exception {
        final AtomicInteger integer = new AtomicInteger();

        Thread t1 = new Thread("t1-> ") {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    int tmp = integer.getAndIncrement();
                    set.add(tmp);
                    System.out.println(Thread.currentThread().getName() + tmp);
                    x++;
                }
            }
        };

        Thread t2 = new Thread("t2-> ") {
            @Override
            public void run() {
                int x = 0;
                while (x < MAX_VALUE) {
                    int tmp = integer.getAndIncrement();
                    set.add(tmp);
                    System.out.println(Thread.currentThread().getName() + tmp);
                    x++;
                }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(set.size());

    }

}
