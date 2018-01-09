package database.presenter;

import dao.DeviceDao;
import database.view.DataBaseView;
import entity.Device;

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
        DeviceDao shadeDao = new DeviceDao();
        List<Device> devices = shadeDao.queryAll();
        Object[][] data=new Object[devices.size()][3];
        for(int i = 0; i< devices.size(); i++){
            Device device = devices.get(i);
            data[i][0]= device.getId();
            data[i][1]= device.getDeviceName();
        }
        mDataBaseView.refresh(data);
    }

    public void open(){
        getData();
    }
}
