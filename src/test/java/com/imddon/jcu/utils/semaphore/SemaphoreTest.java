package com.imddon.jcu.utils.semaphore;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SemaphoreTest {

    interface Advice {
        void before();
        void after();
        void exception(Exception e);
    }

    static class TimeTask implements Runnable {

        private final int timeout;
        private final Semaphore semaphore;
        private final Advice advice;

        public TimeTask(int timeout, Semaphore semaphore, Advice advice) {
            this.timeout = timeout;
            this.semaphore = semaphore;
            this.advice = advice;
        }

        public TimeTask(int timeout, Semaphore semaphore) {
            this.timeout = timeout;
            this.semaphore = semaphore;
            advice = new Advice() {
                    @Override
                    public void before() {
                        log.info("任务开始, {} 获得信号量 剩余信号量：{}", Thread.currentThread().getName(),semaphore.availablePermits());
                    }

                    @Override
                    public void after() {
                        log.info("任务结束, {} 释放信号量 剩余信号量 {}", Thread.currentThread().getName(),semaphore.availablePermits());
                    }

                    @Override
                    public void exception(Exception e) {
                        log.info("出现异常");
                    }
                };
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                advice.before();
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                advice.exception(e);
            } finally {
                semaphore.release();
                advice.after();
            }
        }
    }

    public static class SemaphoreExample01{
        @Test
        public void test1() throws InterruptedException {
            Semaphore semaphore = new Semaphore(2);
            TimeTask timeTask = new TimeTask(5,semaphore);
            Thread t1 = new Thread(timeTask);
            Thread t2 = new Thread(timeTask);
            t1.start();
            TimeUnit.SECONDS.sleep(1);
            t2.start();
            t1.join();
            t2.join();
        }
    }

    public static class SemaphoreExample04{
        @Test
        public void test1() throws InterruptedException {
            Semaphore semaphore = new Semaphore(5);
            Thread t1 = new Thread(() -> {
                semaphore.drainPermits();
                log.info("获取全部信号量");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    log.info("释放全部信号量");
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.info("拿到信号量");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    log.info("释放信号量");
                }
            });
            t1.start();
            TimeUnit.SECONDS.sleep(2);
            t2.start();
            t1.join();
            t2.join();
        }
        @Test
        public void test2() throws InterruptedException {
            Semaphore semaphore = new Semaphore(5);
            Thread t1 = new Thread(() -> {
                semaphore.drainPermits();
                log.info("获取全部信号量");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    log.info("释放全部信号量");
                }
            });
            Thread t2 = new Thread(() -> {
                boolean success = false;
                try {
                    success = semaphore.tryAcquire(1,6,TimeUnit.SECONDS);
                    if (success) log.info("获取成功");
                    else log.info("获取失败");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    if (success) log.info("释放信号量");
                }
            });
            t1.start();
            TimeUnit.SECONDS.sleep(2);
            t2.start();
            t1.join();
            t2.join();
        }
    }

}
