package VolatileDemo;

/*
* volatile是JVM提供的轻量级的同步机制
*   1.保证可见性
*   2.不保证原子性
*   3.禁止指令重拍(有序性)
*
* 可见性：
*   由于各个线程对主内存中共享变量的操作都是各自拷贝到自己的工作内存进行操作后再写回到主内存中的
* 导致的问题，一个线程A修改了共享变量X的值但还未写回主内存中，此时另外一个线程B又对主内存同一个
* 共享变量X进行操作，但此时A线程工作内存中共享变量x对线程B来说并不可见，
* 这种工作内存与主内存同步延迟现象就造成了可见性问题
* */
class MyData{
//    volatile int number = 0;
    int number = 0;
    public void addTo60(){
        this.number = 60;
    }
}

/*
* 验证volatile的可见性
*  结论:volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改。
* */
public class VolatileVisibilityDemo {
    public static void main(String[] args) {
        MyData data = new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t"+data.number);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            data.addTo60();
            System.out.println(Thread.currentThread().getName()+"\t"+data.number);
        },"A").start();

        while(data.number==0){
            //main线程无限循环，直到number!=0
            //如果number数据没用volatile修饰，会一直循环下去
        }
        System.out.println(Thread.currentThread().getName()+"\t"+data.number);

    }
}

