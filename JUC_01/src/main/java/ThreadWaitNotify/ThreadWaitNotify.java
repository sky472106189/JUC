package ThreadWaitNotify;

/**
 * 题目：现在两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替输出，来10轮，变量初始值为零
 *
 * 1.   高内聚低耦合前提下，线程操作资源类
 * 2.   判断/干活/通知
 */
public class ThreadWaitNotify {

    public static void main(String[] args) throws Exception{
        Number number = new Number();
        //增加
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        //减少
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    number.descr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }

}
class Number{//资源类
    private int number = 0;

    public synchronized void add() throws InterruptedException {
        if(number!=0){
            this.wait();
        }
        //干活
        number++;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        //通知
        this.notifyAll();
    };

    public synchronized void descr() throws InterruptedException {
        if(number==0){
            this.wait();
        }
        //干活
        number--;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        //通知
        this.notifyAll();
    };
}