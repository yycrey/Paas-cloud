package paas.rey;

public class SingletonPatternDemo {
    public static void main(String[] args) {
            SingletonObjectTest singletonPatternDemo = SingletonObjectTest.getInstance();
            singletonPatternDemo.showMessage();
    }
}
