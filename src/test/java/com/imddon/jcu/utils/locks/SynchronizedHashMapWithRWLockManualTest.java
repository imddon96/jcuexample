package com.imddon.jcu.utils.locks;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class SynchronizedHashMapWithRWLockManualTest {

    @Test
    public void whenWriting_ThenNoReading() {
        SynchronizedHashMapWithRWLock object = new SynchronizedHashMapWithRWLock();
        final int threadCount = 3;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);

        executeWriterThreads(object, threadCount, service);

        // 需要睡眠，进行正确的判断
        assertFalse(object.isReadLockAvailable());

        service.shutdown();
    }

    @Test
    public void whenReading_ThenMultipleReadingAllowed() {
        SynchronizedHashMapWithRWLock object = new SynchronizedHashMapWithRWLock();
        final int threadCount = 5;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);

        executeReaderThreads(object, threadCount, service);

        assertTrue(object.isReadLockAvailable());

        service.shutdown();
    }

    private void executeWriterThreads(SynchronizedHashMapWithRWLock<String,String> object, int threadCount, ExecutorService service) {
        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                try {
                    object.put("key" + threadCount, "value" + threadCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void executeReaderThreads(SynchronizedHashMapWithRWLock<String,String>  object, int threadCount, ExecutorService service) {
        for (int i = 0; i < threadCount; i++)
            service.execute(() -> object.get("key" + threadCount));
    }

}
