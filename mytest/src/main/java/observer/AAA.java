package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/15.
 */
public class AAA implements Subject {

    private List<Observer> observerList=new ArrayList<>();
    @Override
    public void ddd() {

    }

    public void addObserber(Observer observer){
        observerList.add(observer);
    }

    public void notifyObserver(){
        for (Observer observer:observerList){
            try {
                observer.say();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
