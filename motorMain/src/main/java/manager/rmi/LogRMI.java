package manager.rmi;

import dao.LogDao;
import entity.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LogRMI extends UnicastRemoteObject implements ILog{

    public LogRMI() throws RemoteException {
    }

    @Override
    public List<Log> getLogList() throws RemoteException {
        LogDao logDao = new LogDao();
        List<Log> logList = logDao.queryAll();
        return logList;
    }

    @Override
    public Log getByLogId(int id) throws RemoteException {
        LogDao logDao = new LogDao();
        Log log = logDao.selectByLogId(id);
        return log;
    }
}
