package VolatileDemo;


/*
* 参考脑图【禁止指令重拍小总结】
* */
public class VolatileOrderlyDemo {
    int a = 0;
    boolean flag = false;

    public void method1(){
        a = 1;          // 语句1
        flag = true;    // 语句2
    }

    // 多线程环境中线程交替执行，由于编译器优化重排的存在，
    // 两个线程使用的变量能否保持一致是无法确定的，结果无法预测
    /*
    * 比如：先执行了flag = true;然后由于线程太快，马上执行了语句3 此时a=0 于是结果为5
    * */
    public void method2(){
        if(flag){
            a = a + 5;  // 语句3
            System.out.println(a);
        }
    }
}
