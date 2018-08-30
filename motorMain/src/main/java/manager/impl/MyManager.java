package manager.impl;

import dao.LogDao;
import entity.Log;

public class MyManager {

    private boolean router=true;

    public void init(){
        DatabaseManager.register();
        RestApiManager.init();
        if(router){
            RouterManager.register();
        }
//        SunTrackingManager.init();
    }
}
