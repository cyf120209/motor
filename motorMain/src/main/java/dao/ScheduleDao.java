package dao;

import entity.Schedule;
import mapper.ScheduleMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/6/27.
 */
public class ScheduleDao implements ScheduleMapper {

    @Override
    public List<Schedule> queryAll() {
        SqlSession session = MyBatisUtils.getSession();
        List<Schedule> shadeList;
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            shadeList = mapper.queryAll();
        }finally {
            session.close();
        }
        return shadeList;
    }

    @Override
    public Schedule selectByScheduleId(int id) {
        SqlSession session = MyBatisUtils.getSession();
        Schedule device =null;
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            device = mapper.selectByScheduleId(id);
        }finally {
            session.close();
        }
        return device;
    }

    @Override
    public void insert(Schedule schedule) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            mapper.insert(schedule);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Schedule schedule) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            mapper.update(schedule);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(int scheduleId) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            mapper.delete(scheduleId);
            session.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public void deleteAll() {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            mapper.deleteAll();
            session.commit();
        }finally {
            session.close();
        }
    }
}
