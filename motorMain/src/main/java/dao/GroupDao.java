package dao;

import entity.ShadeGroup;
import mapper.GroupMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/6/28.
 */
public class GroupDao implements GroupMapper {
    @Override
    public List<ShadeGroup> queryAll() throws Exception{
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            return mapper.queryAll();
        }finally {
            session.close();
        }
    }

    @Override
    public ShadeGroup selectByGroupId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            return mapper.selectByGroupId(id);
        }finally {
            session.close();
        }
    }

    @Override
    public void insert(ShadeGroup shadeGroup) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.insert(shadeGroup);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(ShadeGroup shadeGroup) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.update(shadeGroup);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(int id) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.delete(id);
            session.commit();
        }finally {
            session.close();
        }
    }
}
