package com.sky;



public class ThreadLocalTest {
    public class A{
        public static Integer threadLocal = 1;
    }

    public static void main(String[] args) {
        A.threadLocal = 2;
        System.out.println(A.threadLocal);
    }
}
