package observer;

/**
 * Created by lenovo on 2017/6/15.
 */
public class observerTest {

    public static void main(String[] args) {
        AAA aaa = new AAA();
        aaa.addObserber(new Coder());
        aaa.notifyObserver();
    }
}
