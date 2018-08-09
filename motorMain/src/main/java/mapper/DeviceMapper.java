package mapper;

import entity.Device;

import java.util.List;

/**
 * Created by lenovo on 2017/6/18.
 */
public interface DeviceMapper {

    List<Device> queryAll();

    Device selectByDeviceId(int id);

    void insert(Device device);

    void batchInsert(List<Device> deviceList);

    int update(Device device);

    int delete(int shadeId);
}
