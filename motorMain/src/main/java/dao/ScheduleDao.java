package dao;

import entity.Schedule;
import entity.ScheduleGroupRelation;
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
            session.commit();
        }finally {
            session.close();
        }
        return shadeList;
    }

    @Override
    public Schedule selectById(int id) {
        SqlSession session = MyBatisUtils.getSession();
        Schedule device =null;
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            device = mapper.selectById(id);
            session.commit();
        }finally {
            session.close();
        }
        return device;
    }

    @Override
    public long insert(Schedule schedule) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            long insert = mapper.insert(schedule);
            session.commit();
            return schedule.getId();
        }finally {
            session.close();
        }
    }



    @Override
    public int insertRelation(List<ScheduleGroupRelation> scheduleGroupRelationList) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            int i = mapper.insertRelation(scheduleGroupRelationList);
            session.commit();
            return i;
        }finally {
            session.close();
        }
    }

    @Override
    public int update(Schedule schedule) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            int update = mapper.update(schedule);
            session.commit();
            return update;
        }finally {
            session.close();
        }
    }

    @Override
    public int delete(int scheduleId) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            int delete = mapper.delete(scheduleId);
            session.commit();
            return delete;
        }finally {
            session.close();
        }
    }

    @Override
    public int deleteRelation(ScheduleGroupRelation relation) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            int delete = mapper.deleteRelation(relation);
            session.commit();
            return delete;
        }finally {
            session.close();
        }
    }

    @Override
    public int deleteRelation(List<ScheduleGroupRelation> scheduleGroupRelationList) {
        SqlSession session = MyBatisUtils.getSession();
        try {
            int sum=0;
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            for (ScheduleGroupRelation relation:scheduleGroupRelationList){
                int delete = mapper.deleteRelation(relation);
                sum+=delete;
            }
            session.commit();
            return sum;
        }finally {
            session.close();
        }
    }

    @Override
    public int deleteAll() {
        SqlSession session = MyBatisUtils.getSession();
        try {
            ScheduleMapper mapper = session.getMapper(ScheduleMapper.class);
            int i = mapper.deleteAll();
            session.commit();
            return i;
        }finally {
            session.close();
        }
    }
}
