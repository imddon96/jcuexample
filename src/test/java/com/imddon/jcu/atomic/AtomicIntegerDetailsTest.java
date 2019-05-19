package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDetailsTest {

    @Test
    public void test1() {
        // 最快失败的方式，实现不加锁的方式
        AtomicInteger atomicInteger = new AtomicInteger();
        int tmp = atomicInteger.getAndAdd(1);
        System.out.println(tmp);
        System.out.println(atomicInteger.get());
        atomicInteger.compareAndSet(1,0);
        System.out.println(atomicInteger.get());
    }

}
