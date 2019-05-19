package com.imddon.jcu.utils.locks;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class ReentrantLockTest {

    // 可重入锁
    public static class ReentrantLockExample01{
        public static final ReentrantLock lock = new ReentrantLock();


        public static void main(String[] args) throws InterruptedException {
            Thread t1 = new Thread(ReentrantLockExample01::testTryLock);
            Thread t2 = new Thread(ReentrantLockExample01::testTryLock);
            t1.start();
            TimeUnit.SECONDS.sleep(1);
            t2.start();
            TimeUnit.SECONDS.sleep(1);

            t2.interrupt();


        }

        @Test
        public void Test() throws InterruptedException {
            Thread t1 = new Thread(()->{
                needLock();
            });
            Thread t2 = new Thread(()->{
                needLock();
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();

        }
        public static void testTryLock() {

            if (lock.tryLock()) {
                try {
                    log.info("{} 拿到锁 ", Thread.currentThread().getName());
                    while (true) {
                    }
                } finally {
                    log.info("{} 释放锁 ", Thread.currentThread().getName());
                    lock.unlock();
                }
            } else {
                log.info("未获取到锁");
            }
        }
        public static void testUninterrupted() {
            try {
                lock.lockInterruptibly();
                log.info("{} 拿到锁 ", Thread.currentThread().getName());
                while(true) {

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("{} 释放锁 ", Thread.currentThread().getName());
                lock.unlock();
            }
        }

        public static void needLock(){
            try {
                lock.lock();
                log.info("{} 拿到锁 ", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("{} 释放锁 ", Thread.currentThread().getName());
                lock.unlock();
            }
        }

        public static void needLockBySync() {
            synchronized (ReentrantLockExample01.class) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
