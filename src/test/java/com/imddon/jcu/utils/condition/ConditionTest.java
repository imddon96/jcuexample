package com.imddon.jcu.utils.condition;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ConditionTest {

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 完整的生产-消费者模式，使用java.util.concurrent.locks.Condition
    public static class ConditionExample03 {
        private static final ReentrantLock lock = new ReentrantLock();
        private static final Condition PRODUCE_COND = lock.newCondition();
        private static final Condition CONSUME_COND = lock.newCondition();
        private static LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();

        private static final int MAX_CAPACITY = 5;

        public static void main(String[] args) {
            IntStream.range(0, 5).forEach(ConditionExample03::beginProduce);
            IntStream.range(0, 5).forEach(ConditionExample03::beginConsume);
        }

        static void beginProduce(int i) {
            new Thread(() -> {
                for (; ; ) {
                    produce();
                    sleep(1);
                }
            }, "Producer_No." + i).start();
        }

        static void beginConsume(int i) {
            new Thread(() -> {
                for (; ; ) {
                    consume();
                    sleep(1);
                }
            }, "Consumer_No." + i).start();
        }

        static void produce() {
            try {
                lock.lock();
                while (TIMESTAMP_POOL.size() >= MAX_CAPACITY) {
                    PRODUCE_COND.await();
                }
                long value = System.currentTimeMillis();
                TIMESTAMP_POOL.addLast(value);
                System.out.println(Thread.currentThread().getName() + "-P-" + value);
                sleep(1);
                CONSUME_COND.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        static void consume() {
            try {
                lock.lock();
                while (TIMESTAMP_POOL.isEmpty()) {
                    CONSUME_COND.await();
                }
                Long value = TIMESTAMP_POOL.removeFirst();
                System.out.println(Thread.currentThread().getName() + "-C-" + value);
                sleep(1);
                PRODUCE_COND.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }


    public static class ConditionExample02 {
        private static final ReentrantLock lock = new ReentrantLock(true);
        private static final Condition condition = lock.newCondition();

        private static int data = 0;
        private static volatile boolean noUse = false;

        @Test
        public void test() throws InterruptedException {
            new Thread(() -> {
                for (; ; ) {
                    buildData();
                }
            }).start();

            IntStream.range(0, 2).forEach(i -> new Thread(() -> {
                for (; ; ) {
                    useData();
                }
            }).start());

            TimeUnit.SECONDS.sleep(20);
        }

        private static void buildData() {
            try {
                lock.lock();
                data++;
                Optional.of("P: " + data).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private static void useData() {
            try {
                lock.lock();
                Optional.of("C: " + Thread.currentThread().getName() + data).ifPresent(System.out::println);
            } finally {
                lock.unlock();
            }
        }

    }


    public static class ConditionExample01 {

        private static final ReentrantLock lock = new ReentrantLock();
        private static final Condition condition = lock.newCondition();

        private static int data = 0;
        private static volatile boolean noUse = false;

        @Test
        public void test() throws InterruptedException {
            Thread t1 = new Thread(() -> {
                for (; ; ) {
                    buildData();
                }
            });

            Thread t2 = new Thread(() -> {
                for (; ; ) {
                    useData();
                }
            });

            Thread t3 = new Thread(() -> {
                for (; ; ) {
                    useData();
                }
            });

            t1.start();
            t2.start();
            t3.start();

            TimeUnit.SECONDS.sleep(20);
        }

        private static void buildData() {
            try {
                lock.lock();  //synchronized key word monitor in
                while (noUse) {
                    condition.await(); //monitor.wait()
                }
                data++;
                Optional.of("P: " + data).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
                noUse = true;
                condition.signalAll();  //monitor.notify
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); //synchronized key end monitor exit
            }
        }

        private static void useData() {
            try {
                lock.lock();
                while (!noUse) {
                    condition.await();
                }
                TimeUnit.SECONDS.sleep(1);
                Optional.of("C: " + Thread.currentThread().getName() + data).ifPresent(System.out::println);
                noUse = false;
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }

}
