package dao;

import entity.State;
import mapper.GeocoderMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

public class GeocoderDao implements GeocoderMapper {
    @Override
    public List<State> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        List<State> stateList;
        try {
            GeocoderMapper mapper = session.getMapper(GeocoderMapper.class);
            stateList = mapper.queryAll();
        }finally {
            session.close();
        }
        return stateList;
    }

    @Override
    public State selectById(int stateId) {
        return null;
    }

    @Override
    public void insert(State state) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            GeocoderMapper mapper = session.getMapper(GeocoderMapper.class);
            mapper.insert(state);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(State state) {

    }

    @Override
    public void delete(int stateId) {

    }
}
