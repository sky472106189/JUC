package ABA;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            atomicReference.compareAndSet(100,101);
            atomicReference.compareAndSet(101,100);
        },"t1").start();

        new Thread(()->{
            //暂停1秒，保证线程t1完成一次ABA操作
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicReference.compareAndSet(100, 2019) + "\t" + atomicReference.get());
        },"t2").start();

        Thread.sleep(2000);
        System.out.println("=============解决ABA===============");

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t第一次版本号"+stamp);
            //暂停是为了让t3和t4拿到相同的版本号
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicStampedReference.compareAndSet(100,101,stamp,stamp+1);
            System.out.println(Thread.currentThread().getName()+"\t第二次版本号"+atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101,100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t第三次版本号"+atomicStampedReference.getStamp());
        },"t3").start();


        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t第一次版本号"+stamp);
            //暂停是为了让t3和t4拿到相同的版本号
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicStampedReference.compareAndSet(100, 2019,stamp, stamp+1)
                    +"\t 当前版本号" + atomicStampedReference.getStamp()+"\t当前实际最新值"+atomicStampedReference.getReference()
                    +"\t t4期望的版本号"+stamp);
        },"t4").start();
    }
}
