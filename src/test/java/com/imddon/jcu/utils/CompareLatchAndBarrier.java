package com.imddon.jcu.utils;

import org.junit.Test;

import javax.management.AttributeList;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class CompareLatchAndBarrier {


    @Test
    public void testCountDownLatch() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);

        Thread t = new Thread(() -> {
            latch.countDown();
            latch.countDown();
        });
        t.start();
        latch.await();
        assertEquals(0, latch.getCount());
    }

    @Test
    public void testCyclicBarrier() {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        Thread t = new Thread(() -> {
            try {
                barrier.await();
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        t.start();
        assertEquals(1, barrier.getNumberWaiting());
        assertFalse(barrier.isBroken());
    }

    @Test
    public void testCountDownLatchWithoutCyclic() {
        CountDownLatch countDownLatch = new CountDownLatch(7);
        ExecutorService service = Executors.newFixedThreadPool(20);

        AttributeList outputScraper = new AttributeList();
        for (int i = 0; i < 20; i++) {
            service.execute(() -> {
                long preValue = countDownLatch.getCount();
                countDownLatch.countDown();
                if (countDownLatch.getCount() != preValue) {
                    outputScraper.add("Count Updated");
                }
            });
        }
        service.shutdown();
        assertTrue(outputScraper.size() == 7);
    }

    @Test
    public void testCyclicBarrierWithCyclic() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7);
        ExecutorService service = Executors.newFixedThreadPool(20);
        AttributeList outputScraper = new AttributeList();

        for (int i = 0; i < 21; i++) {
            service.execute(()->{
                try {
                    if (cyclicBarrier.getNumberWaiting() <=0 ){
                        outputScraper.add("Count Update");
                    }
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
        assertTrue(outputScraper.size() == 3);
    }


}
