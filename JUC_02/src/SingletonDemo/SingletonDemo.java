package SingletonDemo;

/*
* volatile禁止指令重排
*   单例模式演示
* */
public class SingletonDemo {
//    private static  SingletonDemo instance = null;
    private static volatile SingletonDemo instance = null;

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName()+"\t 执行构造方法");
    }
/*
//    public static synchronized SingletonDemo getInstance(){   用sync修饰也能保证单例模式，但不推荐
    public static SingletonDemo getInstance(){
        if(instance == null){
            instance = new SingletonDemo();
        }
        return instance;
    }*/

    //DCL (Double Check Lock 双端检锁机制)
    //但是DCL不一定线程安全,因为有指令重排的存在,加入volatile可以禁止指令重排
    /*
    * 原因在于某一个线程执行第一次检测，读取到的instance不为null时，instance的引用对象可能没有完成初始化(发生几率可能为0.01%)
    * instance = new SingletonDemo();可以分为以下3步完成(伪代码)
    * memory = allocate(); // 1.分配对象内存空间
    * instance(memory); // 2.初始化对象
    * instance = memory; // 3.设置instance指向刚分配的内存地址,此时instance != null
    *
    * 步骤2和步骤3 不存在数据依赖关系,而且无论重排前还是重排后程序的执行结果在单线程中并没有改变,
    * 因此这种重排优化是允许的，所以会出现以下情况
    * memory = allocate(); // 1.分配对象内存空间
    * instance = memory; // 2.设置instance指向刚分配的内存地址,此时instance != null
    * instance(memory); //3.初始化对象
    * 但是指令重拍只会保证串行语义的执行的一致性(单线程),但并不会关心多线程间的语义一致性。
    * 所以当一条线程访问instance 不为 null时，由于instance实例未必已初始化完成，也就造成了线程安全问题
    * */
    public static SingletonDemo getInstance(){
        if(instance == null){
            synchronized (SingletonDemo.class){
                if(instance == null){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {
        // 单线程
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        // 并发多线程
        for (int i = 1; i <= 10 ; i++) {
            new Thread(()->{
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
