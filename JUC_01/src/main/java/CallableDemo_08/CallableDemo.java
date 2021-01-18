package CallableDemo_08;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("***********come in here");
        return 1024;
    }
}

public class CallableDemo {

    public static void main(String[] args) throws Exception{
        FutureTask futureTask = new FutureTask(new MyThread());
        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start();

        System.out.println(Thread.currentThread().getName()+"*****计算完成");
        System.out.println(futureTask.get());

/*       结果：
         main*****计算完成
         ***********come in here
         1024
*/

        //因为A和B都是用的同一个FuterTask对象,则直接从缓存中读取结果
    }
}
