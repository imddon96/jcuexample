package com.imddon.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class AtomicLongTest {

    /**
     * long 64
     * 分两次执行，高32位，低32位
     * 对数据和地址总线进行佳加锁，使用CPU执行进行操作
     * CMP等指令
     */
    @Test
    public void testCreate() {

        AtomicLong atomicLong = new AtomicLong(100L);
        assertEquals(100L,atomicLong.get());
    }
}
