package ABA;

import java.util.concurrent.atomic.AtomicReference;

/*
* 原子引用
* */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User zs = new User("zs",22);
        User ls = new User("ls",25);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(zs);
        System.out.println(atomicReference.compareAndSet(zs, ls)+"\t" +atomicReference.get().toString());//true	User{userName='ls', age=25}
        System.out.println(atomicReference.compareAndSet(zs, ls)+"\t" +atomicReference.get().toString());//false	User{userName='ls', age=25}

    }
}

class User{
    String userName;
    int age;

    public User() {
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}