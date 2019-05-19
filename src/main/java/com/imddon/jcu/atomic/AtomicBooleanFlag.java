package com.imddon.jcu.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanFlag {

    private final static AtomicBoolean flag = new AtomicBoolean(false);
    // public static volatile boolean flag1 = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                while (!flag.get()) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("I am working ...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("I am finished.");
            }
        }.start();

        Thread.sleep(5000);
        flag.set(true);
    }
}
