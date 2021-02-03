package VolatileDemo;

import java.util.concurrent.atomic.AtomicInteger;

class MyData1{
    volatile int number = 0;

    public void addPlusPlus(){
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    public void addAtomic(){
        atomicInteger.getAndIncrement();
    }
}

/*
* 验证volatile不保证原子性
*   1.原子性是什么
*       不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割。需要整体完整
*       要么同时成功，要么同时失败。
*
*   2.问题描述:
*       假设三个线程t1,t2,t3同时新增，三个线程复制主内存内容至工作内存，然后t1先自增完成，写值给主内存，
*   此时还没来得及更新，t2也自增完成值将值写给主内存了。俗称写覆盖现象。
*
*   3.如何解决原子性
*       * 加sync
*       * 使用java.util.concurrent.atomic包
* */
public class VolatileAtomicDemo {
    public static void main(String[] args) throws InterruptedException {
        MyData1 data1 = new MyData1();
        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000 ; j++) {
                    data1.addPlusPlus();
                    data1.addAtomic();
                }
            },String.valueOf(i)).start();
        }

        //需要等待上面20个线程全部计算完成后，再用main线程取得最终的结果值
        while(Thread.activeCount() > 2){//为什么>2 因为后台默认有2个线程，一个是main线程，一个是GC线程
            Thread.yield();//当前线程暂停,回到就绪状态.注意:在回到就绪状态之后,有可能还会再次抢到
        }

        System.out.println(Thread.currentThread().getName() + "\t 最终值: " + data1.number);//理想值是20000,但实际上不是
        System.out.println(Thread.currentThread().getName() + "\t 保证原子性后最终值: " + data1.atomicInteger);

    }

}
