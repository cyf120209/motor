package dao;

import entity.Shade;
import mapper.ShadeMapper;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import util.MyBatisUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/6/27.
 */
public class ShadeDao implements ShadeMapper {

    @Override
    public List<Shade> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            return mapper.queryAll();
        }finally {
            session.close();
        }
    }

    @Override
    public Shade selectByShadeId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        Shade shade=null;
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            shade = mapper.selectByShadeId(id);
        }finally {
            session.close();
        }
        return shade;
    }

    @Override
    public void insert(Shade shade) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            mapper.insert(shade);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Shade shade) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            mapper.update(shade);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(int shadeId) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            mapper.delete(shadeId);
            session.commit();
        }finally {
            session.close();
        }
    }
}
