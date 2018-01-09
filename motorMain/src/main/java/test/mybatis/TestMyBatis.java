package test.mybatis;

import dao.DeviceDao;
import entity.Device;
import mapper.DeviceMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by lenovo on 2017/6/18.
 */
public class TestMyBatis {

    private SqlSessionFactory sqlSessionFactory;
    private Reader reader;

    public static void main(String[] args) {
        TestMyBatis testMyBatis = new TestMyBatis();
        testMyBatis.init();
//        testMyBatis.testUpdateShade();
//        testMyBatis.testInsertShade();
        testMyBatis.testQueryShade();
//        testMyBatis.testQueryShadeAll();
//        testMyBatis.testDelete();
//        GroupDao groupDao = new GroupDao();
//        try {
//            List<ShadeGroup> shadeGroups = groupDao.queryAll();
//            ShadeGroup shadeGroup = shadeGroups.get(0);
//            List<Device> shades = shadeGroup.getDevices();
//            System.out.println();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ShadeGroup shadeGroup = groupDao.selectByGroupId(1);
//        ShadeGroup shadeGroup = new ShadeGroup(6,6,"name7");
//        shadeGroup.setId(7);
//        groupDao.delete(7);
//        List<Device> shades = shadeGroup.getDevices();
        System.out.println();
    }

    //    @Before
    public void init() {
        try {
            //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            //构建sqlSession的工厂
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    @Test
    public void testDelete() {
        SqlSession session = sqlSessionFactory.openSession();
        try {  //返回值是删除条数
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            deviceMapper.delete(60001);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testInsertShade() {
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            DeviceMapper shadeMapper = session.getMapper(DeviceMapper.class);
//            Device device = new Device(60001, "60001", 0, 0, "0");
//            shadeMapper.add(device);
//            System.out.println("before"+device.getId());
//            session.commit();
//            System.out.println("after"+device.getId());
//        } finally {
//            session.close();
//        }
        Device device = new Device(60001, "60001", "", "", "0");
        DeviceDao shadeDao = new DeviceDao();
        shadeDao.insert(device);
    }

    public void testUpdateShade() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            Device device = deviceMapper.selectByDeviceId(10001);
            deviceMapper.update(device);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testQueryShade() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            Device device = deviceMapper.selectByDeviceId(10001);
//            Device device = (Device) session.selectOne("entity.ShadeShade.selectByID", 1);
            System.out.println(device.getDeviceId());
        } finally {
            session.close();
        }
    }

    public void testQueryShadeAll() {
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            DeviceMapper shadeMapper = session.getMapper(DeviceMapper.class);
//            List<Device> devices = shadeMapper.queryAll();
//            System.out.println(devices.size());
//        } finally {
//            session.close();
//        }
        DeviceDao shadeDao = new DeviceDao();
        List<Device> devices = shadeDao.queryAll();
        Device device = devices.get(0);
    }
}
