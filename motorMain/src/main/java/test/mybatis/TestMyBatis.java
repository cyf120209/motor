package test.mybatis;

import dao.GroupDao;
import dao.ShadeDao;
import entity.Shade;
import entity.ShadeGroup;
import mapper.ShadeMapper;
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
//            List<Shade> shades = shadeGroup.getShades();
//            System.out.println();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ShadeGroup shadeGroup = groupDao.selectByGroupId(1);
//        ShadeGroup shadeGroup = new ShadeGroup(6,6,"name7");
//        shadeGroup.setId(7);
//        groupDao.delete(7);
//        List<Shade> shades = shadeGroup.getShades();
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
            ShadeMapper shadeMapper = session.getMapper(ShadeMapper.class);
            shadeMapper.delete(60001);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testInsertShade() {
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            ShadeMapper shadeMapper = session.getMapper(ShadeMapper.class);
//            Shade shade = new Shade(60001, "60001", 0, 0, "0");
//            shadeMapper.add(shade);
//            System.out.println("before"+shade.getId());
//            session.commit();
//            System.out.println("after"+shade.getId());
//        } finally {
//            session.close();
//        }
        Shade shade = new Shade(60001, "60001", 0, 0, "0");
        ShadeDao shadeDao = new ShadeDao();
        shadeDao.insert(shade);
    }

    public void testUpdateShade() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ShadeMapper shadeMapper = session.getMapper(ShadeMapper.class);
            Shade shade = shadeMapper.selectByShadeId(10001);
            shade.setShadePosition(100);
            shadeMapper.update(shade);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testQueryShade() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ShadeMapper shadeMapper = session.getMapper(ShadeMapper.class);
            Shade shade = shadeMapper.selectByShadeId(10001);
//            Shade shade = (Shade) session.selectOne("entity.ShadeShade.selectByID", 1);
            System.out.println(shade.getShadeId());
            System.out.println(shade.getShadePosition());
        } finally {
            session.close();
        }
    }

    public void testQueryShadeAll() {
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            ShadeMapper shadeMapper = session.getMapper(ShadeMapper.class);
//            List<Shade> shades = shadeMapper.queryAll();
//            System.out.println(shades.size());
//        } finally {
//            session.close();
//        }
        ShadeDao shadeDao = new ShadeDao();
        List<Shade> shades = shadeDao.queryAll();
        Shade shade = shades.get(0);
    }
}
