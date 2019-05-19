package com.imddon.jcu.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDetailsTest {


    public static void main(String[] args) {

        AtomicInteger integer = new AtomicInteger();
        integer.set(1);
        System.out.println(integer.get());
    }

}
