package Lock8_05;

import com.mysql.jdbc.TimeUtil;

import java.util.concurrent.TimeUnit;

/**
 题目:多线程8锁
  1 标准访问,请问先收到邮件还是短信?
       随机(Thread.sleep是为了保证邮件先收到,方便后续学习)，实际上线程的调度是随机的
  2 邮件方法暂停4秒钟，请问先打印邮件还是短信
       邮件。方法内暂停了4秒，是没有释放锁的
 笔记:一个对象里面如果有多个synchronized方法,某一个时刻内,只要一个线程去调用其中的一个synchronized方法了
 其他的线程都只能等待,换句话说,某一个时刻，只能有唯一一个线程去访问这些synchronized方法
 synchronized锁的是当前对象this，被锁定后，其他的线程都不能进入到当前对象的其他的synchronized方法

  3 新增一个普通方法hello(),请问先打印邮件还是hello()
       hello()
 笔记:加普通方法后发现和同步锁无关

  4 两部手机,请问先打印邮件还是短信
       互不影响 随意 先打印短信只是因为邮件延迟了4秒
 笔记:换成两个对象后，不是同一把锁了

  5 两个静态同步方法,同一部手机,请问先打印邮件还是短信
       邮件
  6 两个静态同步方法,两部手机,请问先打印邮件还是短信
       邮件
 笔记:静态锁，锁的是类，也就是类锁。两个手机都是同一个Phone类。
 所以先到的先锁住类，不让其他锁进到同一个类

  7 1个普通同步方法,1个静态同步方法，一部手机,请问先打印邮件还是短信
       互不影响
  8 1个普通同步方法,1个静态同步方法，两部手机,请问先打印邮件还是短信
       互不影响
 笔记:所有的非静态同步方法用的都是同一把锁——实例对象本身(又叫对象锁)
 **同一个对象同一个把锁，static是同一个类同一把锁，对象锁和类锁互不影响**

 synchronized实现同步的基础:Java中每一个对象都可以作为锁。
 具体表现为以下3种形式。
 对于普通同步方法，锁是当前实例对象
 对于静态同步方法，锁是当前类的class对象
 对于同步方法块，锁是synchronized括号里配置的对象

 当一个线程实体访问同步代码块时，它首先必须得到锁，退出或抛出异常时必须释放锁。

 也就是说如果一个实例对象(phone1)的普通同步方法获取锁后，该实例对象的其他普通同步方法必须等待获取锁的方法释放锁后才能获取锁。
 可是另一个的实例对象(phone2)的普通同步方法因为跟该实例对象的普通方法用的是不同的锁，
 所以无需等待该实例对象已获取锁的非静态同步方法释放锁，就可以获取他们自己的锁。

 所有的静态同步方法用的也是同一把锁——类对象本身(类锁)

 这两把锁(this/class)是两个不同的对象，所以静态同步方法与非静态同步方法之间是不会有竞态条件的。
 但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获取锁，
 而不管是同一个实例对象的静态同步方法之间，
 还是不同的实例对象的静态同步方法之间，只要它们是同一个类的实例对象
 */

class Phone{
    public static synchronized void sendEmail() throws Exception{
        //暂停一会儿线程 （同Thread.sleep(400)）
        try{
            //此方法更精确，能精确到更小的秒单位
            TimeUnit.SECONDS.sleep(4);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("------sendEmail!");
    }
    public synchronized void sendSMS() throws Exception{
        System.out.println("------sendSMS");
    }

    public void hello(){
        System.out.println("-----hello!");
    }

}
public class Lock8 {
    public static void main(String[] args) throws InterruptedException{
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(()->{
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"A").start();

        Thread.sleep(100);

        new Thread(()->{
            try {
//                phone.sendSMS();
                phone.hello();
//                phone2.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"B").start();

    }
}
