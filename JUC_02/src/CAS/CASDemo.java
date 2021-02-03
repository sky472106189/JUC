package CAS;

import java.util.concurrent.atomic.AtomicInteger;

/*
* 1.CAS(Compare And Swap)含义
*       比较并交换
*   boolean compareAndSet(int expect, int update)
* 2.CAS举例
*   主物理内存Integer = 5;
*   线程t1 CAS(5,100);
*   线程t2 CAS(5,200);
*   假设线程t1先执行，且比线程t2快得多，那么线程t1拿到了主内存的5,Copy到工作内存，然后比较发现期望值一致，再SET回主内存，并修改值为100
*   此时t2再拿到主内存就会和期望值不一致，不发生改动
* 3.ABA问题
*   CAS会引发ABA问题，比如线程t1和线程t2的CAS期望值和主内存的值相同，假设线程t1和线程t2同时拿到，那么就会发生ABA问题
* */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        // true	 current data:2019
        System.out.println(atomicInteger.compareAndSet(5, 2019)+"\t current data:"+atomicInteger.get());
        // false    current data:2019
        System.out.println(atomicInteger.compareAndSet(5, 1024)+"\t current data:"+atomicInteger.get());

        atomicInteger.getAndIncrement();
    }
}
