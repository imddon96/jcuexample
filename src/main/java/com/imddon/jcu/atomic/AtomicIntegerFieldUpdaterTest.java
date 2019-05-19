package com.imddon.jcu.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterTest {

    public static void main(String[] args) throws NoSuchFieldException {

        /**
         *
         */
        final AtomicIntegerFieldUpdater<TestMe> updater =
                AtomicIntegerFieldUpdater.newUpdater(TestMe.class,"i");

        final TestMe me = new TestMe();
        final int MAX = 20;

        for (int i = 0; i < 2; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < MAX; i++) {
                        int v = updater.getAndIncrement(me);
                        System.out.println(Thread.currentThread().getName() + "=>" +v);
                    }
                }
            }.start();
        }
    }

    static class TestMe {
        protected volatile  int i;
    }
    private  class Test{

    }

}
