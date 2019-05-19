package com.imddon.jcu.utils.cyclicbarrier;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class Solver implements Runnable {
    private static final int N = 10;
    private final CyclicBarrier barrier;


    public Solver() {
        this.barrier = new CyclicBarrier(N, this);
    }

    public void run() {
        try {
            mergeRows();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void mergeRows() throws InterruptedException {
        Thread.sleep(2000);
        log.info("All rows merged");
    }

    static class Worker implements Runnable {
        private int i;
        private CyclicBarrier barrier;

        public Worker(int i, CyclicBarrier barrier) {
            this.i = i;
            this.barrier = barrier;
        }

        public void run() {
            try {
                processRow();
                int tmp = barrier.await();
/*                if(tmp == 9 || tmp == 1)
                    log.info(Thread.currentThread().getName()+"All rows merged");*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void processRow() throws InterruptedException {
            Thread.sleep(1000);
            log.info("The {} row processed", this.i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Solver solver = new Solver();
        List<Thread> threads = new ArrayList<>(N);

        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Worker(i, solver.barrier));
            thread.setName("(name-"+ i +")");
            threads.add(thread);
            thread.start();
        }

        log.info("{}",solver.barrier.getNumberWaiting());

        for (Thread thread : threads) {
            thread.join();
        }
        log.info("{}",solver.barrier.getNumberWaiting());

        log.info(Thread.currentThread().getName()+" quit.");

    }
}
