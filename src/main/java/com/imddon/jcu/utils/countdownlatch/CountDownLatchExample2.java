package com.imddon.jcu.utils.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class CountDownLatchExample2 {

    public static void main(final String[] args) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        new Thread("sss") {
            @Override
            public void run() {
                log.info("Do something initial working.");
                try {
                    Thread.sleep(1000);
                    latch.await();
                    log.info("Do other working...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("bbb") {
            @Override
            public void run() {
                log.info("before await");
                try {
                    latch.await();
                    log.info("after await");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("aaa") {
            @Override
            public void run(){
                log.info("asyn prepare for some data ");
                try {
                    Thread.sleep(2000);
                    log.info("data prepare for done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        }.start();

        Thread.currentThread().join();
    }
}
