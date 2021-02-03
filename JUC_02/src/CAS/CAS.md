#### CAS底层原理?如果知道，谈谈你对UnSafe的理解

​    **CAS底层原理:就是自旋锁和UnSafe类**

- CAS的全称为Compare-And-Swap,**它是一条CPU并发原语**。

  它的功能是判断内存某个位置的值是否为预期值，如果是则更改为新的值，这个过程有原子性的。

- CAS并发原语体现在Java语言中就是sun.misc.Unsafe类中的各个方法。调用UnSafe类的CAS方法，JVM会帮我们实现出CAS汇编指令。

  - CAS汇编指令是一种完全依赖于硬件的功能，通过它实现了原子操作

- 再次强调，由于CAS是一种系统原语，原语属于操作系统用语范畴，是由若干条指令组成的，用于完成某个功能的一个过程，**并且原语具有原子性，执行必须是连续的，执行过程不允许被中断，也就是说CAS是一条CPU的原子指令，不会造成所谓的数据不一致问题。**

#### 从AtomicInteger.getAndIncrement()了解UnSafe类

- getAndIncrement()源码:

  ```java
  public final int getAndIncrement() {
      return unsafe.getAndAddInt(this, valueOffset, 1);
  }
  ```

这里使用了unsafe类的方法，引出了unsafe

![image-20210203095038875](https://typora-1303202660.cos.ap-nanjing.myqcloud.com/undefined/202102/03/145606-78573.png)

从源码中可以看出 volatile修饰的value，保证了可见性和有序性

valueOffset就是内存地址偏移量（C的指针能够直接操作内存地址）



#### UnSafe类

- 是CAS的核心类，由于Java方法无法直接访问底层系统，需要通过本地（native）方法来访问，Unsafe相当于一个后门，基于该类可以直接操作特定内存的数据。==Unsafe类存在于sun.misc包中==，其内部方法操作可以像C的指针一样直接操作内存，因为Java中CAS操作的执行以来Unsafe类的方法

  ***注意Unsafe类中的所有方法都是native修饰的，也就是说Unsafe类中的方法都直接调用操作系统底层资源***

-  变量ValueOffset，表示该变量值在内存中的偏移地址，因为Unsafe就是根据内存偏移地址来获取数据的

  ![image-20210203095250645](https://typora-1303202660.cos.ap-nanjing.myqcloud.com/undefined/202102/03/145725-374801.png)

- 变量value用volatile修饰，保证了多线程之间的可见性

##### 源码分析

```java
//unsafe.getAndAddInt源码
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

> 以 AtomicInteger.getAndIncrement()举例
>
> ​	分析源码：
>
> ​	var5是通过var1 var2找出主内存中真实的值，用该对象的值与var5比较：
>
> - 如果相同，更新var5 + va4 并且返回true
> - 如果不同，继续取值然后再比较，直到更新完成

- var5 = getIntValatile(var1,var2)
  - 就是得到var1对象对应的内存地址var2的值



##### 场景举例

- AtomicInteger里面的value原始值为3，即主内存中AtomicInteger的value为3，根据JMM模型，线程A和线程B各自持有一份值为3的副本到各自的工作内存

- 线程A通过getIntVolatile(var1,var2)拿到value的值3，这时线程A被挂起
- 线程B也通过getIntVolatile(var,var2)方法获取到value值3，此时刚好线程B**没有被挂起**并执行compareAndSwapInt方法比较内存值也为3，成功修改内存值为4，线程B该玩收工。
- 这时线程A恢复，执行compareAndSwapInt方法比较，发现自己工作内存的值3和主内存的值4不一致，说明该值已经被其他线程抢先一步修改过了，那A线程本次修改失败，**只能重新读取再来一遍**
- 线程A重新获取value值，因为value被volatile修饰，所以其他线程对它的修改，线程A总是能够看到的，线程A继续执行compareAndSwapInt进行比较替换，直到成功

#### 总结

##### CAS(CompareAndSwap)

比较当前工作内存中的值和主内存中的值，如果相同则执行规定操作，否则继续比较直到主内存和工作内存中的值一致为止

##### CAS应用

CAS有3个操作数，内存值V，旧的预期值A，要修改的更新至B。

当且仅当预期值A和内存值B相同时，将内存值V修改为B，否则什么都不做。

#### CAS缺点

- 循环时间长开销很大
- 只能保证一个共享变量的原子操作
- ABA问题