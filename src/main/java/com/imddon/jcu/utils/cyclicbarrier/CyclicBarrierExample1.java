package com.imddon.jcu.utils.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CyclicBarrierExample1 {

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        final CyclicBarrier barrier = new CyclicBarrier(2);

        new Thread ("Thread-LongW") {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.info(Thread.currentThread().getName()+" await");
                    barrier.await();
                    log.info(Thread.currentThread().getName()+" finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        new Thread ("Thread-SlowW") {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    log.info(Thread.currentThread().getName()+" await");
                    barrier.await();
                    log.info(Thread.currentThread().getName()+" finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();



        TimeUnit.MILLISECONDS.sleep(500);
        log.info("Before reset : {}",barrier.getNumberWaiting());
        barrier.reset();
        TimeUnit.MILLISECONDS.sleep(600);
        log.info("After reset : {} ;;; and main await",barrier.getNumberWaiting());
        barrier.await();

    }



}
