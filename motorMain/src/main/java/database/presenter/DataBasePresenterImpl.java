package database.presenter;

import dao.ShadeDao;
import database.view.DataBase;
import database.view.DataBaseView;
import entity.Shade;

import java.util.List;

/**
 * Created by lenovo on 2017/8/24.
 */
public class DataBasePresenterImpl {

    private DataBaseView mDataBaseView;

    public DataBasePresenterImpl(DataBaseView dataBaseView) {
        this.mDataBaseView=dataBaseView;
    }

    void getData(){
        ShadeDao shadeDao = new ShadeDao();
        List<Shade> shades = shadeDao.queryAll();
        Object[][] data=new Object[shades.size()][3];
        for(int i=0;i<shades.size();i++){
            Shade shade = shades.get(i);
            data[i][0]=shade.getId();
            data[i][1]=shade.getShadeName();
            data[i][2]=shade.getShadeStatus();
        }
        mDataBaseView.refresh(data);
    }

    public void open(){
        getData();
    }
}
