package dao;

import entity.Shade;
import mapper.ShadeMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/6/27.
 */
public class ShadeDao implements ShadeMapper {

    @Override
    public List<Shade> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        List<Shade> shadeList;
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            shadeList = mapper.queryAll();
        }finally {
            session.close();
        }
        return shadeList;
    }

    @Override
    public Shade selectByShadeId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        Shade device =null;
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            device = mapper.selectByShadeId(id);
        }finally {
            session.close();
        }
        return device;
    }

    @Override
    public void insert(Shade device) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            mapper.insert(device);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Shade device) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
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
            ShadeMapper mapper = session.getMapper(ShadeMapper.class);
            mapper.delete(shadeId);
            session.commit();
        }finally {
            session.close();
        }
    }
}
