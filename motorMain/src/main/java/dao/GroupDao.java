package dao;

import entity.ShadeGroup;
import entity.ShadeGroupRelation;
import mapper.GroupMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/28.
 */
public class GroupDao implements GroupMapper {
    @Override
    public List<ShadeGroup> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        List<ShadeGroup> shadeGroupList=new ArrayList<>();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            shadeGroupList.addAll( mapper.queryAll());
        } finally {
            session.close();
        }
        return shadeGroupList;
    }

    @Override
    public ShadeGroup selectByGroupId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            return mapper.selectByGroupId(id);
        } finally {
            session.close();
        }
    }

    @Override
    public ShadeGroup selectByGroupOther(ShadeGroup shadeGroup) {
        SqlSession session = MyBatisUtils.getSession();
        ShadeGroup sg=null;
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            sg = mapper.selectByGroupOther(shadeGroup);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return sg;
    }

    @Override
    public int insert(ShadeGroup shadeGroup) {
        int id;
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            id = mapper.insert(shadeGroup);
            session.commit();
        } finally {
            session.close();
        }
        return id;
    }

    @Override
    public void insertRelation(List<ShadeGroupRelation> shadeGroupRelationList) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.insertRelation(shadeGroupRelationList);
            session.commit();
        } finally {
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
        } finally {
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
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteByShadeGroup(ShadeGroup shadeGroup) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.deleteByShadeGroup(shadeGroup);
            session.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteRelation(List<ShadeGroupRelation> shadeGroupRelationList) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.deleteRelation(shadeGroupRelationList);
            session.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteRelationById(int id) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            mapper.deleteRelationById(id);
            session.commit();
        } finally {
            session.close();
        }
    }
}
