package NewThreadWaitNotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
    新的生产者/消费者写法

    使用Lock和Condition接口来实现

    一、Lock接口：
    实现类：
    public class ReentrantLock
        extends Object
        implements Lock, Serializable

    public void lock()  获得锁。
    如果锁没有被另一个线程占用并且立即返回，则将锁定计数设置为1。
    如果当前线程已经保持锁定，则保持计数增加1，该方法立即返回。
    如果锁被另一个线程保持，则当前线程将被禁用以进行线程调度，并且在锁定已被获取之前处于休眠状态，此时锁定保持计数被设置为1。

    public void unlock() 尝试释放此锁。
    如果当前线程是该锁的持有者，则保持计数递减。 如果保持计数现在为零，则锁定被释放。
    如果当前线程不是该锁的持有者，则抛出IllegalMonitorStateException 。


    二、Condition接口
    使用Lock.newCondition()方法返回新的Condition

    Condition newCondition()
    返回一个新Condition绑定到该实例Lock实例。
    在等待条件之前，锁必须由当前线程保持。 呼叫Condition.await()将在等待之前将原子释放锁，并在等待返回之前重新获取锁。

    void await()
        throws InterruptedException  导致当前线程等到发信号或interrupted 。

    void signalAll()
        唤醒所有等待线程。
    如果任何线程正在等待这个条件，那么它们都被唤醒。 每个线程必须重新获取锁，才能从await返回。

*/

public class NewThreadWaitNotify {

    public static void main(String[] args) {
        Number number = new Number();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }

}

//资源类
class Number{
    private int number = 0;
    private Lock lock = new ReentrantLock();//多态,父类型引用指向子类型对象
    private Condition condition= lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        try{
            while(number != 0){
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try{
            while(number == 0){
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}