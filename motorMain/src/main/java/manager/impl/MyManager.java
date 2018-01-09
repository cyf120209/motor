package manager.impl;

public class MyManager {

    private boolean router=false;

    public void init(){
        DatabaseManager.register();
        RestApiManager.init();
        if(router){
            RouterManager.register();
        }
    }
}
