package org.elpmis.inaction.chapter07;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TaskCancellation {

    static final ExecutorService service = Executors.newFixedThreadPool(5);

    public static void timeRun(Runnable r, long timeout, TimeUnit uint) {
        Future<?> task = service.submit(r);
        try {
            task.get(timeout,uint);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            log.info("任务执行超时...");
        } finally {
            boolean success = task.cancel(false); //不会抛出中断异常
            if (success) {
                log.info("未完成，任务取消成功");
            } else {
                log.info("取消失败，任务已经完成");
            }
            service.shutdown();
        }
    }

    public static void main(String[] args) {
        Object lock = new Object();
        Runnable runnable = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("任务被中断，但是中断标志位已经被擦除");
            }

            System.out.println("任务结束");
        };
        timeRun(runnable,2,TimeUnit.SECONDS); //阻塞
    }

}
