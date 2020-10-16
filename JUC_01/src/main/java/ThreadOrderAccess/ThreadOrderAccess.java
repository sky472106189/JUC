package ThreadOrderAccess;

/*
    Episode.4
    多线程之间按顺序调用 实现A->B->c
    三个线程启动,要求如下:

    AA打印5次,BB打印10次,CC打印15次
    接着
    AA打印5次,BB打印10次,CC打印15次
    ........来10轮

多线程口诀：
1.   高内聚低耦合前提下，线程操作资源类
2.   判断/干活/通知
3.   多线程交互中，必须要防止多线程中的虚假唤醒，也即(判断使用While，不能用if)
             因为if是判断，while是循环,虚假唤醒是因为没有循环的原因
4.   标志位
*/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadOrderAccess {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();

/*        new Thread(()->{
            shareResource.print5();
        },"A").start();
        new Thread(()->{
            shareResource.print10();
        },"B").start();
        new Thread(()->{
            shareResource.print15();
        },"C").start();*/

        new Thread(()->{
            shareResource.print("A");
        },"A").start();
        new Thread(()->{
            shareResource.print("B");
        },"B").start();
        new Thread(()->{
            shareResource.print("C");
        },"C").start();
    }
}

class ShareResource{
    private int number = 1;//1:A 2:B 3:C; 即标志位
    private Lock lock = new ReentrantLock();
    //三把钥匙,对应同一把锁。A钥匙;B钥匙;C钥匙
    private Condition condition_A = lock.newCondition();
    private Condition condition_B = lock.newCondition();
    private Condition condition_C = lock.newCondition();

    /*public void print5(){
        lock.lock();
        try {
            while(number != 1){
                condition_A.await();
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            number = 2;
            condition_B.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print10(){
        lock.lock();
        try{
            while(number != 2){
                condition_B.await();
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            number = 3;
            condition_C.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print15(){
        lock.lock();
        try{
            while(number != 3 ){
                condition_C.await();
            }
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            number = 1;
            condition_A.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }*/

    public void print(String type){
        if(type.equals("A")){
            lock.lock();
            try {
                while( number != 1){
                    condition_A.await();
                }
                for (int i = 0; i <= 5 ; i++) {
                    System.out.println(Thread.currentThread().getName()+"\t"+i);
                }
                number = 2;
                condition_B.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }else if(type.equals("B")){
            lock.lock();
            try {
                while( number != 2){
                    condition_B.await();
                }
                for (int i = 0; i <= 10 ; i++) {
                    System.out.println(Thread.currentThread().getName()+"\t"+i);
                }
                number = 3;
                condition_C.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }else if(type.equals("C")){
            lock.lock();
            try {
                while( number != 3){
                    condition_C.await();
                }
                for (int i = 0; i <= 15 ; i++) {
                    System.out.println(Thread.currentThread().getName()+"\t"+i);
                }
                number = 1;
                condition_A.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}