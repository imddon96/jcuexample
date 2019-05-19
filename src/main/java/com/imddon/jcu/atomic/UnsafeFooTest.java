package com.imddon.jcu.atomic;


import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class UnsafeFooTest {


    public static void main(String[] args) throws IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {

/*
        Simple simple = new Simple();
        System.out.println(simple.get());
        Simple.class.newInstance();
*/

        Unsafe unsafe = UnsafeTest.getUnsafe();

/*        Simple simple = (Simple) unsafe.allocateInstance(Simple.class);
        System.out.println(simple.getClass().getClassLoader());
        System.out.println(simple.get());*/

/*        Guard guard = new Guard();
        guard.work();

        Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        unsafe.putInt(guard,unsafe.objectFieldOffset(f),42);
        guard.work();*/

/*        byte[] bytes = loadClassContent();
        assert unsafe != null;
        Class aClass = unsafe.defineClass(null, bytes, 0, bytes.length, UnsafeFooTest.class.getClassLoader(), null);
        int v = (Integer) aClass.getMethod("get").invoke(aClass.newInstance());

        System.out.println(v);*/

        System.out.println(sizeOf(new Simple()));

    }

    private static long sizeOf(Object obj) {
        Unsafe unsafe = UnsafeTest.getUnsafe();
        Set<Field> fields = new HashSet<Field>();

        Class c = obj.getClass();

        while (c != Object.class) {
            Field[] declaredFields = c.getDeclaredFields();

            for (Field f : declaredFields) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
                c = c.getSuperclass();
            }
        }

        long maxOffset = 0;

        for (Field f : fields) {
            long offset = unsafe.objectFieldOffset(f);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        return (maxOffset/4)+1;

    }


    /**
     * load class content
     *
     * @return
     * @throws IOException
     */
    private static byte[] loadClassContent() throws IOException {
        File f = new File("/Users/imddon/Codes/A.class");

        FileInputStream fileInputStream = new FileInputStream(f);

        byte[] content = new byte[(int) f.length()];

        fileInputStream.read(content);
        fileInputStream.close();
        return content;

    }

    static class Guard {
        private int ACCESS_ALLOWED = 1;

        private boolean allow() {
            return 42 == ACCESS_ALLOWED;
        }

        public void work() {
            if (allow()) {
                System.out.println("I am working by allowed");
            }
        }
    }

    static class Simple {
        private int i = 0;


        public Simple() {
            this.i = 1;
            System.out.println("==========");
        }

        public int get() {
            return this.i;
        }
    }
}
