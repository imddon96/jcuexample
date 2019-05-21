package com.imddon.jcu.utils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class StampedLockTest {

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class StampedLockExample02 {

        private final static StampedLock lock = new StampedLock();
        private final static List<Long> DATA = new ArrayList<>();

        public static void main(String[] args) {
            ExecutorService service = Executors.newFixedThreadPool(100);
            Runnable readTask = ()->{
                for(;;){
                    read();
                }
            };

            for(int i = 0; i < 20; i++) {
                service.submit(readTask);
            }

            while(true){
                service.submit(StampedLockExample02::write);
                sleep(5);
            }
        }

        static void read(){
            long stamp = lock.tryOptimisticRead();
            if (!lock.validate(stamp)) {
                try {
                    stamp = lock.readLock();
                    Optional.of(
                            DATA.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))
                    ).ifPresent(System.out::println);
                } finally {
                    lock.unlockRead(stamp);
                }
            } else {
                Optional.of(
                        DATA.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))
                ).ifPresent(System.out::println);
            }
            sleep(1);
        }
        static void write(){
            long stamp = -1;
            try {
                stamp = lock.writeLock();
                DATA.add(System.currentTimeMillis());
                System.out.println("写操作");
                sleep(1);
            } finally {
                lock.unlockWrite(stamp);
            }
        }
    }

    // 悲观锁导致多个读线程一直等待一个读线程写，而且会阻塞
    public static class StampedLockExample01 {

        private final static StampedLock lock = new StampedLock();
        private final static List<Long> DATA = new ArrayList<>();

        public static void main(String[] args) {
            ExecutorService service = Executors.newFixedThreadPool(100);
            Runnable readTask = ()->{
                for(;;){
                    read();
                }
            };

            for(int i = 0; i < 20; i++) {
                service.submit(readTask);
            }

            while(true){
                service.submit(StampedLockExample02::write);
                sleep(5);
            }

        }

        static void read(){
            long stamp = -1;
            try {
                stamp = lock.readLock();
                Optional.of(
                    DATA.stream().map(String::valueOf).collect(Collectors.joining("#","R-",""))
                ).ifPresent(System.out::println);
                sleep(1);
            } finally {
                lock.unlockRead(stamp);
            }
        }
        static void write(){
            long stamp = -1;
            try {
                stamp = lock.writeLock();
                DATA.add(System.currentTimeMillis());
                System.out.println("写操作");
                sleep(1);
            } finally {
                lock.unlockWrite(stamp);
            }
        }
    }
}
