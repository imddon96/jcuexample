package com.imddon.jcu.utils.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockWithCondition<T> {

    private final Stack<T> stack = new Stack();
    private static final int CAPACITY = 5;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition stackFullCondition = lock.newCondition();
    private final Condition stackEmptyCondition = lock.newCondition();

    private void pushToStack(T item) throws InterruptedException {
        try {
            lock.lock();
            if (stack.size() == CAPACITY) {
                log.info(Thread.currentThread().getName() + " wait on stack full");
                stackFullCondition.await();
            }
            log.info("Pushing the item " + item);
            stack.push(item);
            stackEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private T popFromStack() throws InterruptedException {
        try {
            lock.lock();
            if (stack.size() == 0) {
                log.info(Thread.currentThread().getName() + " wait on stack empty");
                stackEmptyCondition.await();
            }
            return stack.pop();
        } finally {
            stackFullCondition.signalAll();
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final int threadCount = 2;
        ReentrantLockWithCondition object = new ReentrantLockWithCondition();
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        service.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    object.pushToStack("Item " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    log.info("Item popped " + object.popFromStack());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.shutdown();
    }
}


