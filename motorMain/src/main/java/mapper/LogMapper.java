package mapper;

import entity.Log;

import java.util.List;

public interface LogMapper {

    List<Log> queryAll();

    Log selectByLogId(int logId);

    void insert(Log log);

    void update(Log log);

    void delete(int logId);
}
