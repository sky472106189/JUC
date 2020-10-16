package ThreadWaitNotify;

/**
 * Episode.2
 *
 * 题目：现在两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替输出，来10轮，变量初始值为零
 *
 * 1.   高内聚低耦合前提下，线程操作资源类
 * 2.   判断/干活/通知
 * 3.   多线程交互中，必须要防止多线程中的虚假唤醒，也即(判断使用While，不能用if)
 *              因为if是判断，while是循环,虚假唤醒是因为没有循环的原因
 */
public class ThreadWaitNotify {

    public static void main(String[] args) throws Exception{
        Number number = new Number();
        //生产
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                    number.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        //消费
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(200);
                    number.descr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        //生产
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(300);
                    number.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        //消费
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(400);
                    number.descr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }

}
class Number{//资源类
    private int number = 0;

    public synchronized void add() throws InterruptedException {
        while(number!=0){
            this.wait();
        }
        //干活
        number++;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        //通知
        this.notifyAll();
    };

    public synchronized void descr() throws InterruptedException {
        while(number==0){
            this.wait();
        }
        //干活
        number--;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        //通知
        this.notifyAll();
    };
}