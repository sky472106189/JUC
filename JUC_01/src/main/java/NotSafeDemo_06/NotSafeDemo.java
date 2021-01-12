package NotSafeDemo_06;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
  举例说明不安全的集合类
 1.故障现象
    java.util.ConcurrentModificationException
    并发修改异常
 2.导致原因
    场景举例:签到花名册,此时小明和小红同时进来签名，只有一支笔。
 小明在写"小日"明字还没写完的时候，小红把笔抢走了。由于小红一扯，
 导致纸上被笔花了长长的一道导致了并发修改异常。

 3.解决方案
    3.1 使用线程安全的集合类代替 Vector()
    3.2 Collections.synchronizedList(new ArrayList<>())
    3.3 CopyOnWriteArrayList<>()
        3.3.1 场景举例:
    签到花名册,此时有老师监督，一张写满了名字的花名册，小明和小红进来签名，只有一支笔。
 此时老师让小明先来，但是不是直接在此花名册上签，而是复制了一份花名册v1.1给小明，同时把原件
 贴在了黑板上，给其他同学看。小明写上了自己的名字到花名册V1.1后，递给老师。老师把花名册V1.1贴在黑板上
 接着小红来签名，同理会复制一份花名册V1.1的复制件花名册V1.2，然后小红拿走V1.2去签名。签完名后，
 老师再接着去用V1.2贴在黑板上。
    这样就实现了 读写分析，也就是读和写不冲突


 4.优化建议（同样的错误，不出现第2次）

 5.CopyOnWriteArrayList<>()
    线程安全的原理分析:
 写时复制
 CopyOnWrite容器即写时复制的容器。往一个容器添加元素的时候,不直接往当前容易Object[]添加，而是先将容器Object[]进行Copy
 复制出一个新的容器Object[] newElements，然后新的容器Object[] newElements里添加元素，添加完元素之后，
 再将原容器的引用指向新的容器 setArray(newElements)。
 这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种
 读写分离的思想，读和写不同的容器
 源码:
 public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
 }

 */
public class NotSafeDemo {
    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        List<String> list = new Vector<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }

    }
}
