package dao;

import entity.Log;
import mapper.LogMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

public class LogDao implements LogMapper {

    @Override
    public List<Log> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        List<Log> logList;
        try {
            LogMapper mapper = session.getMapper(LogMapper.class);
            logList = mapper.queryAll();
        }finally {
            session.close();
        }
        return logList;
    }

    @Override
    public Log selectByLogId(int logId) {
        return null;
    }

    @Override
    public synchronized void insert(Log log) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            LogMapper mapper = session.getMapper(LogMapper.class);
            mapper.insert(log);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Log log) {

    }

    @Override
    public void delete(int logId) {

    }
}
