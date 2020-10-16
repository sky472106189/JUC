package SaleTicket;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
* Episode.1
*
* 案例：三个售票员  卖出  30张票
*
* 1. 高内聚低耦合前提下，线程 操作 资源类
* */
public class SaleTicket {
    public static void main(String[] args) throws Exception{

        Ticket ticket = new Ticket();

        new Thread(()->{for(int i = 1;i<=40;i++)ticket.saleTicket();},"A").start();
        new Thread(()->{for(int i = 1;i<=40;i++)ticket.saleTicket();},"B").start();
        new Thread(()->{for(int i = 1;i<=40;i++)ticket.saleTicket();},"C").start();


/*      Thread(Runnable target, String name)
          target - 启动此线程时调用其run方法的对象。 如果null ，则调用此线程的run方法。
          name - 新线程的名称
*/
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1;i<=40;i++){
                    ticket.saleTicket();
                }
            }
        },"A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1;i<=40;i++){
                    ticket.saleTicket();
                }
            }
        },"B").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1;i<=40;i++){
                    ticket.saleTicket();
                }
            }
        },"C").start();
*/
    }
}

class Ticket {//资源类
    private int number = 30;
    private final Lock lock = new ReentrantLock();

    public  void saleTicket(){
        lock.lock();
        try {
            if(number>0){
                System.out.println(Thread.currentThread().getName()+"\t卖出第"+(number--)+"\t还剩下:"+number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

}