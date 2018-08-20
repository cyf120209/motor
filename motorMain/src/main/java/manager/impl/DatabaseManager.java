package manager.impl;

import com.serotonin.bacnet4j.RemoteDevice;
import dao.DeviceDao;
import dao.GeocoderDao;
import dao.GroupDao;
import dao.ShadeDao;
import entity.*;
import redis.clients.jedis.Jedis;
import util.MyBatisUtils;
import util.MyLocalDevice;
import util.Public;
import util.RemoteUtils;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class DatabaseManager {

    Map<Integer,Device> mLastDeviceMap=new HashMap<>();

    public static void register(){
        //初始化数据库
        MyBatisUtils.init();
        //周期性更新数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DatabaseManager().updateDatabase();
            }
        }).start();
    }

    private void updateDatabase() {
//        GeocoderDao geocoderDao = new GeocoderDao();
//        List<State> stateList = geocoderDao.queryAll();
//        System.out.println(""+stateList.size());
//        Jedis jedis = new Jedis("localhost");
//        jedis.auth("foobared");
//        System.out.println(jedis.ping());
//        Device device = new Device(10001, "", "", "", "");
//        jedis.set("device", device);

//        DeviceDao deviceDao = new DeviceDao();
//        List<Device> deviceList = deviceDao.queryAll();
//        for (Device device:deviceList){
//            mLastDevices.put(device.getDeviceId(),device);
//        }
        Timer timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                long lastTime=System.nanoTime();

                RemoteUtils mRemoteUtils = MyLocalDevice.mRemoteUtils;
                if(mRemoteUtils==null){
                    return;
                }
                updateDevice(mRemoteUtils);
                updateShades();
                long currentTime=System.nanoTime();
                System.out.println("----device--------time----------- "+(currentTime-lastTime)/1000000);
                updateGroup();


//                Iterator<Map.Entry<Integer, Map<Integer, List<Integer>>>> iterator1 = rMap.entrySet().iterator();
            }
        });
        timer.start();
    }

    private void updateShades() {
        DeviceDao deviceDao = new DeviceDao();
        List<Device> deviceList = deviceDao.queryAll();
        for (Device device:deviceList){
            if(Public.matchString(device.getModelName(),"-AC-")){

            }
        }
    }

    private void updateGroup() {
        //更新关系列表
        //从数据库中取出数据，整理成map
        GroupDao groupDao = new GroupDao();
        List<ShadeGroup> shadeGroupList = groupDao.queryAll();
        Map<Integer, Map<Integer, List<Integer>>> rMap=new HashMap<>();
        for (ShadeGroup shadeGroup:shadeGroupList){
            Integer deviceId = shadeGroup.getDeviceId();
            Map<Integer, List<Integer>> shadeGroupMap = rMap.get(deviceId);
            if(shadeGroupMap==null){
                shadeGroupMap=new HashMap<>();
                rMap.put(deviceId,shadeGroupMap);
            }
            Integer groupId = shadeGroup.getGroupId();
            List<Integer> shadeList = shadeGroupMap.get(groupId);
            if(shadeList==null){
                shadeList=new ArrayList<>();
                shadeGroupMap.put(groupId,shadeList);
            }
            List<Shade> list = shadeGroup.getShades();
            for (Shade shade:list) {
                if(shade==null){
                    continue;
                }
                Integer shadeId = shade.getShadeId();
                if (!shadeList.contains(shadeId)) {
                    shadeList.add(shadeId);
                }
            }
        }
        //取出缓存中的数据
        Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.getRelationMap();

        // 遍历缓存中map，若数据库中没有这个组，则添加
        Iterator<Map.Entry<Integer, Map<Integer, List<Integer>>>> iterator = relationMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, Map<Integer, List<Integer>>> entry = iterator.next();
            Integer deviceId = entry.getKey();
            // 缓存  组 - 电机列表 map
            Map<Integer, List<Integer>> sg = entry.getValue();
            // 数据库  组 - 电机列表 map
            Map<Integer, List<Integer>> gMap = rMap.remove(deviceId);
            if(gMap==null){
                gMap=new HashMap<>();
            }

            Iterator<Map.Entry<Integer, List<Integer>>> iterator1 = sg.entrySet().iterator();
            while (iterator1.hasNext()){
                Map.Entry<Integer, List<Integer>> shadeGroupRelation = iterator1.next();
                Integer groupId = shadeGroupRelation.getKey();
                //缓存 电机列表
                List<Integer> shadeList = shadeGroupRelation.getValue();
                // 数据库 电机列表
                List<Integer> sDeviceIdList = gMap.remove(groupId);
                if(sDeviceIdList==null){
                    sDeviceIdList=new ArrayList<>();
                }
                ShadeGroup sGroup = new ShadeGroup(groupId, deviceId);
                //查询组是否存在，若不存在则添加
                ShadeGroup shadeGroup = groupDao.selectByGroupOther(sGroup);
                if(shadeGroup==null){
                    groupDao.insert(sGroup);
                    shadeGroup=sGroup;
                }
                List<ShadeGroupRelation> relationList=new ArrayList<>();
                List<ShadeGroupRelation> toDelRelationList=new ArrayList<>();
                //缓存-〉数据库
                //遍历缓存中的电机列表，若数据库中无此电机，则添加
                if(shadeList!=null){
                    for (Integer i:shadeList){
                        if(!sDeviceIdList.contains(i)){
                            relationList.add(new ShadeGroupRelation(shadeGroup.getId(), i));
                            System.out.println("size: "+shadeList.size());
                            System.out.println("add   shadeGroupId: "+ shadeGroup.getId()+" shade: "+i);
                        }
                    }
                    if(relationList!=null && relationList.size()!=0){
                        groupDao.insertRelation(relationList);
                    }
                }
                //数据库-〉缓存
                //遍历数据库中的电机列表，若缓存中无此电机，则删除
                if(sDeviceIdList!=null){
                    for (Integer j:sDeviceIdList){
                        if(!shadeList.contains(j)){
                            toDelRelationList.add(new ShadeGroupRelation(shadeGroup.getId(), j));
                            System.out.println("delete   shadeGroupId: "+ shadeGroup.getId()+" shade: "+j);
                        }
                    }
                    if(toDelRelationList!=null && toDelRelationList.size()!=0){
                        groupDao.deleteRelation(toDelRelationList);
                    }
                }
            }
            if(gMap==null){
                continue;
            }
            Iterator<Map.Entry<Integer, List<Integer>>> iterator2 = gMap.entrySet().iterator();
            if(iterator2.hasNext()){
                Map.Entry<Integer, List<Integer>> next = iterator2.next();
                Integer key = next.getKey();
                if(next.getValue()==null || next.getValue().size()==0){
                    ShadeGroup shadeGroup = new ShadeGroup(key,deviceId);
                    groupDao.deleteByShadeGroup(shadeGroup);
                    System.out.println("delete   deviceId: "+ deviceId+" groupId"+key);
                    continue;
                }
                //删除关系列表
                for (ShadeGroup _sg:shadeGroupList){
                    if(_sg.getGroupId().intValue()==key.intValue() && _sg.getDeviceId().intValue()==deviceId.intValue()){
                        groupDao.deleteRelationById(_sg.getId());
                        System.out.println("delete   shadeGroupId: "+ _sg.getId());
                        break;
                    }
                }
                //删除组
                ShadeGroup shadeGroup = new ShadeGroup(key,deviceId);
                groupDao.deleteByShadeGroup(shadeGroup);
                System.out.println("delete   deviceId: "+ deviceId+" groupId"+key);
            }

        }
        //剩下的数据库rMap 为要删除的数据
        Iterator<Map.Entry<Integer, Map<Integer, List<Integer>>>> toDeleteMapMap = rMap.entrySet().iterator();
        while (toDeleteMapMap.hasNext()){
            Map.Entry<Integer, Map<Integer, List<Integer>>> _toDeleteMapMap = toDeleteMapMap.next();
            Integer deviceId = _toDeleteMapMap.getKey();
            Map<Integer, List<Integer>> toDeleteMap = _toDeleteMapMap.getValue();
            Iterator<Map.Entry<Integer, List<Integer>>> _toDeleteMap = toDeleteMap.entrySet().iterator();
            while (_toDeleteMap.hasNext()){
                Map.Entry<Integer, List<Integer>> toDelete = _toDeleteMap.next();
                Integer groupId = toDelete.getKey();
                List<Integer> shadeList = toDelete.getValue();
                //查询待删除的组的id
                ShadeGroup sGroup = new ShadeGroup(groupId, deviceId);
                ShadeGroup shadeGroup = groupDao.selectByGroupOther(sGroup);
                //遍历数据库中的电机列表，全部删除
                List<ShadeGroupRelation> toDelRelationList=new ArrayList<>();
                if(shadeList!=null){
                    for (Integer j:shadeList){
                        toDelRelationList.add(new ShadeGroupRelation(shadeGroup.getId(), j));
                        System.out.println("delete   shadeGroupId: "+ shadeGroup.getId()+" shade: "+j);
                    }
                    if(toDelRelationList!=null && toDelRelationList.size()!=0){
                        groupDao.deleteRelation(toDelRelationList);
                    }
                }
                //删除组
                groupDao.deleteByShadeGroup(shadeGroup);
                System.out.println("delete   shadeGroupId: "+ shadeGroup.getId());
            }

        }
    }

    public static void updateGroupThread(){
        new DatabaseManager().updateGroup();
    }

    private void updateDevice(RemoteUtils mRemoteUtils) {
        Queue<Integer> remoteDeviceIdDbAdd = mRemoteUtils.getRemoteDeviceIdDbAdd();
        Queue<Integer> remoteDeviceIdDbDelete = mRemoteUtils.getRemoteDeviceIdDbDelete();
        List<Device> deviceListAdd=new ArrayList<>();

        DeviceDao deviceDao = new DeviceDao();
        List<Device> deviceDbList = deviceDao.queryAll();
        List<RemoteDevice> deviceList = MyLocalDevice.getRemoteDeviceList();
        Map<Integer, RemoteDevice> remoteDeviceMap = mRemoteUtils.getRemoteDeviceMap();

//                System.out.println("+++++add++++++++++"+remoteDeviceIdDbAdd.size());
//                System.out.println("+++++del++++++++++"+remoteDeviceIdDbDelete.size());
        //增加
        while (!remoteDeviceIdDbAdd.isEmpty()){
            boolean isAdd=true;
            Integer id = remoteDeviceIdDbAdd.poll();
            for (int j = 0; j < deviceDbList.size(); j++) {
                Device device = deviceDbList.get(j);
                Integer deviceId = device.getDeviceId();
                if(id.intValue() == deviceId.intValue()){
                    isAdd=false;
                    break;
                }
            }
            if(!isAdd){
                continue;
            }
            System.out.println("add++++++"+id);
            //插入数据库
            RemoteDevice remoteDevice = remoteDeviceMap.get(id);
            int instanceNumber = remoteDevice.getInstanceNumber();
            String mac = remoteDevice.getAddress().getMacAddress().toString();
            String modelName = Public.readModelName(remoteDevice);
            String version = Public.readVersion(remoteDevice);
            String name = remoteDevice.getName();
            name=(name==null || "".equals(name))? instanceNumber+"_"+modelName : name;
            Device d = new Device(instanceNumber, name, mac, modelName, version,"");
            deviceListAdd.add(d);
            //单条插入
            deviceDao.insert(d);
        }
        //批量插入
//                if(deviceListAdd.size()>0){
//                    deviceDao.batchInsert(deviceListAdd);
//                }

        if(deviceDbList.size()==0){
            return;
        }
        //删除设备
        while (!remoteDeviceIdDbDelete.isEmpty()){
            Integer deviceId = remoteDeviceIdDbDelete.poll();
            for (int j = 0; j < deviceDbList.size(); j++) {
                Device device = deviceDbList.get(j);
                if(deviceId.intValue() == device.getDeviceId().intValue()){
                    //删除记录
                    System.out.println("del++++++"+deviceId);
                    deviceDao.delete(deviceId);
                    break;
                }
            }
        }

        //更新设备
        for (RemoteDevice remoteDevice:deviceList){
            if(remoteDeviceIdDbDelete.contains(new Integer(remoteDevice.getInstanceNumber()))){
                continue;
            }
            int instanceNumber = remoteDevice.getInstanceNumber();
            String mac = remoteDevice.getAddress().getMacAddress().toString();
            String modelName = Public.readModelName(remoteDevice);
            String version = Public.readVersion(remoteDevice);
            Device d = new Device(instanceNumber, mac, modelName, version);
            if(!isInList(deviceDbList,d)){
                deviceDao.update(d);
            }
        }
    }

    private boolean isInList(List<Device> deviceDbList,Device d){
        for (Device device:deviceDbList){
            if(device.equals(d)){
                return true;
            }
        }
        return false;
    }
}
