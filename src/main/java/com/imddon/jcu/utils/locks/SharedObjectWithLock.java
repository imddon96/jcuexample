package com.imddon.jcu.utils.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

@Slf4j
public class SharedObjectWithLock {

    private ReentrantLock lock = new ReentrantLock(true);

    private int counter = 0;

    void perform() {

        lock.lock();
        log.info("Thread - " + Thread.currentThread().getName() + " acquired the lock");
        try {
            log.info("Thread - " + Thread.currentThread().getName() + " processing");
            counter++;
            sleep(200);
        } catch (Exception exception) {
            log.error(" Interrupted Exception ", exception);
        } finally {
            lock.unlock();
            log.info("Thread - " + Thread.currentThread().getName() + " released the lock");
        }
    }

    private void performTryLock() {

        log.info("Thread - " + Thread.currentThread().getName() + " attempting to acquire the lock");
        try {
            boolean isLockAcquired = lock.tryLock(2, TimeUnit.SECONDS);
            if (isLockAcquired) {
                try {
                    log.info("Thread - " + Thread.currentThread().getName() + " acquired the lock");

                    log.info("Thread - " + Thread.currentThread().getName() + " processing");
                    sleep(1000);
                } finally {
                    lock.unlock();
                    log.info("Thread - " + Thread.currentThread().getName() + " released the lock");

                }
            }
        } catch (InterruptedException exception) {
            log.error(" Interrupted Exception ", exception);
        }
        log.info("Thread - " + Thread.currentThread().getName() + " could not acquire the lock");
    }

    public ReentrantLock getLock() {
        return lock;
    }

    boolean isLocked() {
        return lock.isLocked();
    }

    boolean hasQueuedThreads() {
        return lock.hasQueuedThreads();
    }

    int getCounter() {
        return counter;
    }

    public static void main(String[] args) {

        final int threadCount = 2;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        final SharedObjectWithLock object = new SharedObjectWithLock();

        service.execute(object::perform);
        service.execute(object::performTryLock);

        service.shutdown();

    }


}
