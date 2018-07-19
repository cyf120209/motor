package util;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import entity.Device;
import model.DeviceGroup;

import java.util.*;

/**
 * Created by lenovo on 2017/4/18.
 */
public class RemoteUtils {

    /**
     * 远程设备 数据库增加队列
     */
    private Queue<Integer> remoteDeviceIdDbAdd =new PriorityQueue<>();

    /**
     * 远程设备 数据库增加队列
     */
    private Queue<Integer> remoteDeviceIdDbDelete =new PriorityQueue<>();

    /**
     * 远程设备 地址列表
     */
    private Set<Byte> remoteDeviceAddrSet=new HashSet<>();

    /**
     * 远程设备 地址-Id
     */
    private Map<Byte,Integer> remoteDeviceAddrIdMap=new HashMap<>();

    /**
     * 去重处理
     */
    private Set<Device> deviceSet=new HashSet<>();

    /**
     * 远程设备 地址-Id
     */
    private Map<Byte,Device> remoteDeviceByteDeviceMap=new HashMap<>();

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
    public synchronized void addRemoteDevice(Device device,RemoteDevice remoteDevice){
        byte mstpAddress = remoteDevice.getAddress().getMacAddress().getMstpAddress();
        remoteDeviceAddrSet.add(mstpAddress);
        deviceSet.add(device);
        remoteDeviceByteDeviceMap.put(mstpAddress,device);
        mRemoteDeviceIDList.add(remoteDevice.getInstanceNumber());
        mRemoteDeviceList.add(remoteDevice);
        //远程设备列表 Map
        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
        //数据库
        remoteDeviceAddrIdMap.put(mstpAddress,remoteDevice.getInstanceNumber());
        remoteDeviceIdDbAdd.offer(remoteDevice.getInstanceNumber());
    }

    /**
     * 更新远程设备
     * @param remoteDevice
     */
    public synchronized void updateRemoteDevice(RemoteDevice remoteDevice){
        mRemoteDeviceList.remove(mRemoteDevice.get(remoteDevice));
//        String modelName = Public.readModelName(remoteDevice);
//        remoteDevice.setModelName(modelName);
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

    public void addRemoteDeviceAddr(Byte addr){
        remoteDeviceAddrSet.add(addr);
    }

    public boolean deleteRemoteDeviceAddr(Byte addr){
        Integer integer = remoteDeviceAddrIdMap.get(addr);
        if(integer !=null){
            remoteDeviceIdDbDelete.offer(integer);
            deviceSet.remove(remoteDeviceByteDeviceMap.get(addr));
            remoteDeviceByteDeviceMap.remove(addr);
            remoteDeviceAddrIdMap.remove(addr);
            mRemoteDeviceList.remove(mRemoteDevice.get(integer));
            mRemoteDevice.remove(integer);
            mRemoteDeviceIDList.remove(integer);

        }
        return remoteDeviceAddrSet.remove(addr);
    }

    public Set<Device> getDeviceSet() {
        return deviceSet;
    }

    public Queue<Integer> getRemoteDeviceIdDbAdd() {
        return remoteDeviceIdDbAdd;
    }

    public Queue<Integer> getRemoteDeviceIdDbDelete() {
        return remoteDeviceIdDbDelete;
    }
}
