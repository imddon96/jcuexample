package com.imddon.jcu.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS的好处以及带来的问题：可见行，有序性，原子性
 * volatile 保证来前面两个性质
 * CAS算法：也就是CPU级别的同步指令，相当于乐观锁，它可以检测到其他线程对共享数据的变化
 *
 * 采取最快失败策略
 *
 * CAS轻量级锁带来的严重的问题  AB ACD问题
 *
 * 如何解决这种问题呢？  添加版本
 *
 */

public class AtomicReferenceTest {


    public static void main(String[] args) {
        Simple simple = new Simple("Alex",19);

        AtomicReference<Simple> atomicSimple = new AtomicReference<Simple>(simple);

        boolean flag = atomicSimple.compareAndSet(new Simple("Alex",19),new Simple("Anny",20));

        System.out.println(flag);
    }


    static class Simple{
        private String name;
        private int age;

        public Simple(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
