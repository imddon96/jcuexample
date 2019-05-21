package com.imddon.jcu.utils.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.sleep;

@Slf4j
public class SynchronizedHashMapWithRWLock<K,V>{

    private final Map<K, V> syncHashMap = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void put(K key, V value) throws InterruptedException {
        try {
            writeLock.lock();
            log.info(Thread.currentThread().getName() + " writing");
            syncHashMap.put(key,value);
            sleep(1000);
        } finally {
            writeLock.unlock();
        }
    }

    public  V get(K key) {
        try {
            readLock.lock();
            log.info(Thread.currentThread().getName() + " reading");
            return syncHashMap.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public V remove(K key) {
        try {
            writeLock.lock();
            return syncHashMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean containsKey(K key) {
        try {
            readLock.lock();
            return syncHashMap.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    boolean isReadLockAvailable() {
        return readLock.tryLock();
    }

    public static void main(String[] args) throws InterruptedException {

        final int threadCount = 3;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        SynchronizedHashMapWithRWLock<String,String> object = new SynchronizedHashMapWithRWLock<>();

        service.execute(new Thread(new Writer(object), "Writer"));
        service.execute(new Thread(new Reader(object), "Reader1"));
        service.execute(new Thread(new Reader(object), "Reader2"));

        service.shutdown();
    }

    private static class Reader implements Runnable {

        SynchronizedHashMapWithRWLock<String,String> object;

        Reader(SynchronizedHashMapWithRWLock<String,String> object) {
            this.object = object;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                log.info("get---->{}",object.get("key" + i));
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Writer implements Runnable {

        SynchronizedHashMapWithRWLock<String,String> object;

        Writer(SynchronizedHashMapWithRWLock<String,String> object) {
            this.object = object;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    object.put("key" + i, "value" + i);
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
