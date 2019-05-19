package com.imddon.jcu.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class CompareAndSetLock {

    private final AtomicInteger value = new AtomicInteger();

    private Thread thread;  //谁锁的锁，就谁来开锁

    public void tryLock() throws GetLockException {
        boolean success = false;
        success = value.compareAndSet(0, 1);
        if (!success)
            throw new GetLockException("Get the lock failed");
        else
            thread = Thread.currentThread();
    }

    public void unlock() {
        if (0 == value.get())
            return;
        if (thread == Thread.currentThread())
            value.compareAndSet(1,0);
    }
}
