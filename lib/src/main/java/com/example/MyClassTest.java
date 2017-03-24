package com.example;


class A{
    public void method1(){
        System.out.println("--A----method1 ");
    }
    public int num = 100;

    public void method2(){
//        局部内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                method1();
                //匿名内部类可以访问外部类的成员方法
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("------hello world-------");
                        method1();
                        num = 1000;
                    }
                }).start();
            }
        }).start();
    }
}

public class MyClassTest {

    public static void main(String[] args){
        A a = new A();
        a.method2();
    }
}
