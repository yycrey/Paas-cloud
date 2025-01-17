package paas.rey;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/17
 * @Param
 * @Exception
 **/
public class SingletonObjectTest {
    //创建一个当前类的对象
    private static final SingletonObjectTest INSTANCE =new SingletonObjectTest();

    //不会被实例化
    private SingletonObjectTest(){

    }

    //获取唯一可用对象
    public static SingletonObjectTest getInstance(){
        return INSTANCE;
    }

    public void showMessage(){
        System.out.println("hello world");
    }
}

