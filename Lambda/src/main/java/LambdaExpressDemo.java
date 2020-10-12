/**
 * 2 Lambda Express
 *      2.1 口诀：
 *          拷贝小括号，写死右箭头，落地大括号
 *
 *      2.2 @FunctionalInterface
 *
 *      2.3 default jdk8 方法默认实现
 *
 *      2.4 静态方法实现
 */


@FunctionalInterface //函数式接口：接口只有一个方法
interface Foo{
    //接口有且仅有一个方法,才能使用Lambda
//    public void sayHello();
    public int add(int x,int y);

    //默认方法实现
    default int div(int x,int y){
        System.out.println("div result is :");
        return x/y;
    }

    public static int mv(int x,int y){
        return x * y;
    }
}


public class LambdaExpressDemo {
    public static void main(String[] args) {
        /*Foo foo = new Foo() {
            @Override
            public void sayHello() {
                System.out.println("hello java02222");
            }
        };
        foo.sayHello();*/

        Foo foo = (int x,int y) -> {
            System.out.println("add result is :");
            return x + y;
        };
        System.out.println(foo.add(1,2));
        System.out.println(foo.div(4,2));
        System.out.println(Foo.mv(3,2));
    }
}

