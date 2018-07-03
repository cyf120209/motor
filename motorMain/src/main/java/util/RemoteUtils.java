package util;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import model.DeviceGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/18.
 */
public class RemoteUtils {

    /**
     * 远程设备ID列表
     */
    private List<Integer> mRemoteDeviceIDList=new ArrayList<>();

    /**
     * 远程设备列表 List
     */
    private List<RemoteDevice> mRemoteDeviceList=new ArrayList<>();

    /**
     * 远程设备列表 Map
     */
    private Map<Integer, RemoteDevice> mRemoteDevice = new HashMap<>();

    /**
     * device，group，draper 关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mMap=new HashMap<Integer, Map<Integer, List<Integer>>>();

    private List<DeviceGroup> deviceGroupList=new ArrayList<>();

    private static Object lock=new Object();
    public RemoteUtils() {
    }

    /**
     * 判断设备是否已添加
     * @param remoteDevice
     * @return
     */
    public boolean isExist(RemoteDevice remoteDevice){
        return mRemoteDeviceList.contains(remoteDevice);
    }


    /**
     * 添加远程设备
     * @param remoteDevice
     */
    public synchronized void addRemoteDevice(RemoteDevice remoteDevice){
        if(isExist(remoteDevice)){
            return;
        }
        String modelName = Public.readModelName(remoteDevice);
        remoteDevice.setModelName(modelName);
        mRemoteDeviceIDList.add(remoteDevice.getInstanceNumber());
        mRemoteDeviceList.add(remoteDevice);
        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
    }

    /**
     * 更新远程设备
     * @param remoteDevice
     */
    public synchronized void updateRemoteDevice(RemoteDevice remoteDevice){
        mRemoteDeviceList.remove(mRemoteDevice.get(remoteDevice));
        String modelName = Public.readModelName(remoteDevice);
        remoteDevice.setModelName(modelName);
        mRemoteDeviceList.add(remoteDevice);
        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
    }

    /**
     * 清空 mRemoteDeviceList,mRemoteDevice
     */
    public void clearRemoteDevice(){
        mRemoteDeviceList.clear();
        mRemoteDevice.clear();
    }

    public Map<Integer, Map<Integer, List<Integer>>> getRelationMap() {
        return mMap;
    }

    public synchronized void setRelationMap(Map<Integer, Map<Integer, List<Integer>>> map) {
        mMap.clear();
        mMap.putAll(map);
    }

    public List<DeviceGroup> getDeviceGroupList() {
        return deviceGroupList;
    }

    public void setDeviceGroupList(List<DeviceGroup> deviceGroupList) {
        List<DeviceGroup> dgList=new ArrayList<>();
        deviceGroupList.clear();
        deviceGroupList.addAll(dgList);
    }

    /**
     * 获取远程设备ID列表
     * @return
     */
    public List<Integer> getRemoteDeviceIDList() {
        List<Integer> remoteDeviceIDList = new ArrayList<>();
        remoteDeviceIDList.addAll(mRemoteDeviceIDList);
        return remoteDeviceIDList;
    }

    /**
     * 获取远程设备列表
     * @return
     */
    public List<RemoteDevice> getRemoteDeviceList() {
        List<RemoteDevice> remoteDeviceList = new ArrayList<>();
        remoteDeviceList.addAll(mRemoteDeviceList);
        return remoteDeviceList;
    }


    /**
     * 获取远程设备列表
     * @return
     */
    public List<RemoteDevice> getRemoteDeviceList(String tag) {
        List<RemoteDevice> remoteDeviceList = new ArrayList<>();
        for(RemoteDevice remoteDevice:mRemoteDeviceList){
            if("".equals(remoteDevice.getModelName())){
                remoteDeviceList.add(remoteDevice);
            }
        }
        return remoteDeviceList;
    }

    /**
     * 获取deviceID，device的关系列表
     * @return
     */
    public Map<Integer, RemoteDevice> getRemoteDeviceMap() {
        return mRemoteDevice;
    }

}
