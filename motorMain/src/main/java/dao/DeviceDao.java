package dao;

import entity.Device;
import mapper.DeviceMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/6/27.
 */
public class DeviceDao implements DeviceMapper {

    @Override
    public List<Device> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            return mapper.queryAll();
        }finally {
            session.close();
        }
    }

    @Override
    public Device selectByDeviceId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        Device device =null;
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            device = mapper.selectByDeviceId(id);
        }finally {
            session.close();
        }
        return device;
    }

    @Override
    public void insert(Device device) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            Device d = mapper.selectByDeviceId(device.getDeviceId());
            if(d==null){
                mapper.insert(device);
            }else {
                d.setIsShow(1);
                mapper.update(d);
            }
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void batchInsert(List<Device> deviceList) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            mapper.batchInsert(deviceList);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Device device) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            mapper.update(device);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(int shadeId) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            mapper.delete(shadeId);
            session.commit();
        }finally {
            session.close();
        }
    }
}
