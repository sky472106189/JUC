package Lock8_05;

/*
类锁不会继承,只会影响当前这个类
即父类锁和子类锁不互斥
*/

public class Lock9 {
    public static void main(String[] args) throws InterruptedException {
        A a = new A();
//        A a2 = new A();

        new Thread(()->{
            a.doSome();
        },"A").start();

        Thread.sleep(100);

        new Thread(()->{
            a.doOther();
        },"B").start();
    }

}

class A extends B{
    public static synchronized void doOther(){
        System.out.println("doOther begin");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doOther end");
    }
}

class B{

    public static synchronized void doSome(){
        System.out.println("doSome begin");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSome end");
    }


}
