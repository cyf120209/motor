package util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by lenovo on 2017/6/27.
 */
public class MyBatisUtils {


    private static Reader reader;

    private static SqlSessionFactory sessionFactory;

//    private static SqlSession session;

    static {
        try {
            //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            //构建sqlSession的工厂
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(){

    }

    public static SqlSession getSession(){
        return sessionFactory.openSession();
    }

    public static void closeSession(SqlSession session){
        session.close();
    }
}
