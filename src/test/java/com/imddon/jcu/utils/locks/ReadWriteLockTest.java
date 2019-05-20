package com.imddon.jcu.utils.locks;

import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class ReadWriteLockTest {

    // 使用 java.util.concurrent.locks.ReentrantReadWriteLock
    public static class ReadWriteLockExample02{
        private static final ReadWriteLock lock = new ReentrantReadWriteLock();
        private static final Lock readLock = lock.readLock();
        private static final Lock writeLock = lock.writeLock();
        private static final List<Long> data = new ArrayList<>();

        // 读写锁分离，当没有写入工作时，读的效率会大大增加
        @Test
        public void test1() throws InterruptedException {

            Thread thread1 = new Thread(ReadWriteLockExample02::write);
            thread1.start();
            TimeUnit.SECONDS.sleep(1);
            Thread thread2 = new Thread(ReadWriteLockExample02::read);
            thread2.start();
            TimeUnit.SECONDS.sleep(1);

            IntStream.range(0,5).forEach(i->new Thread(ReadWriteLockExample02::read).start());

            TimeUnit.SECONDS.sleep(5);
        }
        static void write(){
            try {
                writeLock.lock();
                data.add(System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }
        static void read(){
            try{
                readLock.lock();
                data.forEach(System.out::println);
            } finally {
                readLock.unlock();
            }
        }

    }

    // 使用 java.util.concurrent.locks.ReentrantLock
    public static class ReadWriteLockExample01 {
        // 构造的锁为不公平锁
        private static final ReentrantLock lock = new ReentrantLock();
        private static final List<Long> data = new ArrayList<>();
        // 两个线程同时去读，便会降低性能；当只有两个线程去读的时候，不应该加锁；当有线程去写时，读才要上锁；
        @Test
        public void test1() throws InterruptedException {
            Thread thread1 = new Thread(ReadWriteLockExample01::write);
            Thread thread2 = new Thread(ReadWriteLockExample01::read);
            thread1.start();
            TimeUnit.SECONDS.sleep(1);
            thread2.start();
            TimeUnit.SECONDS.sleep(20);
        }
        static void write() {
            try {
                lock.lock();
                data.add(System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        static void read() {
            try {
                lock.lock();
                data.forEach(System.out::println);
            } finally {
                lock.unlock();
            }
        }
    }

}
