package observer;

/**
 * Created by lenovo on 2017/6/15.
 */
public class Coder implements Observer {

    @Override
    public void say(){
        System.out.println("打你");
    }
}
