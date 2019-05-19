package com.imddon.jcu.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class Test {


    public static abstract class Animal {
        public abstract void eat();
    }

    public static class Dog extends  Animal {
        public void eat() {
            System.out.println("Dog is eating");
        }
        public void speack() {
            System.out.println("wo wo wo ~");
        }
    }

    public static class Cat extends  Animal {
        public void eat() {
            System.out.println("Cat is eating");
        }
    }

    public static void f(Animal animal) {
        animal.eat();
    }

    public static void main(String[] args) {

//        f(new Dog());
//        f(new Cat());
//
//        Animal animal  = new Dog();


        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);  //不会再赋值，只是将引用指向常量中首次出现的实例应用

        str1 = null;
        System.gc();

        String str3 = new StringBuilder("计算").append("机软件").toString(); //在heap中新建一个对象
        System.out.println(str3.intern() == str3); //常量池中

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);

        log.info("Aaa");

        IntStream.rangeClosed(1,3).forEach(i-> System.out.println(i));

    }


}
