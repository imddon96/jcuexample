package com.imddon.jcu.atomic;

public class AtomicIntegerDetailsTest2 {

    private final static CompareAndSetLock lock = new CompareAndSetLock();


    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    doSomething();
                } catch (GetLockException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void doSomething() throws GetLockException, InterruptedException {
        try {
            lock.tryLock();
            System.out.println(Thread.currentThread().getName() + " get the lock");
            Thread.sleep(100000);
        } finally {
            lock.unlock();
        }
    }
}
