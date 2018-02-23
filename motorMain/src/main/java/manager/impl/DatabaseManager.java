package manager.impl;

import com.serotonin.bacnet4j.RemoteDevice;
import dao.DeviceDao;
import dao.GroupDao;
import dao.ShadeDao;
import entity.Device;
import entity.Shade;
import entity.ShadeGroup;
import entity.ShadeGroupRelation;
import redis.clients.jedis.Jedis;
import util.MyBatisUtils;
import util.MyLocalDevice;
import util.Public;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class DatabaseManager {

    public static void register(){
        //初始化数据库
        MyBatisUtils.init();
        //周期性更新数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
//                new DatabaseManager().updateDatabase();
            }
        }).start();
    }

    private void updateDatabase() {
        Jedis jedis = new Jedis("localhost");
        jedis.auth("foobared");
        System.out.println(jedis.ping());
        Device device = new Device(10001, "", "", "", "");
//        jedis.set("device", device);

        Timer timer = new Timer(15000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                GroupDao groupDao1 = new GroupDao();
//                ShadeGroup shadeGroup1 = new ShadeGroup(3, 2);
//                groupDao1.insert(shadeGroup1);
//                List<ShadeGroupRelation> relationList1=new ArrayList<>();
//                for (int i=0;i<6;i++){
//                    relationList1.add(new ShadeGroupRelation(shadeGroup1.getId(), i));
//                }
//                groupDao1.insertRelation(relationList1);

//                List<ShadeGroupRelation> toDelRelationList1=new ArrayList<>();
//                toDelRelationList1.add(new ShadeGroupRelation(1,10001));
//                toDelRelationList1.add(new ShadeGroupRelation(1,10002));
//                groupDao1.deleteRelation(toDelRelationList1);

                //更新电机
                List<RemoteDevice> deviceList = MyLocalDevice.getRemoteDeviceList();
                //
//                deviceList.add(new RemoteDevice(10001,new Address(1,(byte) 01),null));
//                deviceList.add(new RemoteDevice(10002,new Address(2,(byte) 02),null));
//                String modelName="123";
//                String version="321";
                //
                for (RemoteDevice remoteDevice:deviceList){
                    DeviceDao deviceDao = new DeviceDao();
                    Device device = deviceDao.selectByDeviceId(remoteDevice.getInstanceNumber());
                    int instanceNumber = remoteDevice.getInstanceNumber();
                    String mac = remoteDevice.getAddress().getMacAddress().toString();
                    String modelName = Public.readModelName(remoteDevice);
                    String version = Public.readVersion(remoteDevice);
                    String name = remoteDevice.getName();
                    ShadeDao shadeDao = new ShadeDao();
                    if(device==null){
                        deviceDao.insert(new Device(instanceNumber,name,mac,modelName,version));
                        shadeDao.insert(new Shade(instanceNumber,name,0,0,"1"));
                    }else {
                        deviceDao.update(new Device(instanceNumber,name,mac,modelName,version));
                        shadeDao.update(new Shade(instanceNumber,name,0,0,"1"));
                    }
                }

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

                //模拟缓存中的map
                List<Integer> integerList = new ArrayList<>();
                integerList.add(new Integer(10001));
                integerList.add(new Integer(10002));
                Map<Integer,List<Integer>> mmm=new HashMap<>();
                mmm.put(new Integer(111),integerList);
                mmm.put(new Integer(222),integerList);
                mmm.put(new Integer(444),integerList);
                relationMap.put(new Integer(1),mmm);

                // 遍历缓存中map，若数据库中没有这个组，则添加
                Iterator<Map.Entry<Integer, Map<Integer, List<Integer>>>> iterator = relationMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<Integer, Map<Integer, List<Integer>>> entry = iterator.next();
                    Integer deviceId = entry.getKey();
                    Map<Integer, List<Integer>> sg = entry.getValue();
                    Map<Integer, List<Integer>> gMap = rMap.remove(deviceId);
                    if(gMap==null){
                        gMap=new HashMap<>();
                    }

                    Iterator<Map.Entry<Integer, List<Integer>>> iterator1 = sg.entrySet().iterator();
                    while (iterator1.hasNext()){
                        Map.Entry<Integer, List<Integer>> shadeGroupRelation = iterator1.next();
                        Integer groupId = shadeGroupRelation.getKey();
                        List<Integer> shadeList = shadeGroupRelation.getValue();
                        List<Integer> sDeviceIdList = gMap.remove(groupId);
                        if(sDeviceIdList==null){
                            sDeviceIdList=new ArrayList<>();
                        }
                        ShadeGroup sGroup = new ShadeGroup(groupId, deviceId);
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
                            continue;
                        }
                        for (ShadeGroup _sg:shadeGroupList){
                            if(_sg.getGroupId().intValue()==key.intValue() && _sg.getDeviceId().intValue()==deviceId.intValue()){
                                groupDao.deleteRelationById(_sg.getId());
                                break;
                            }
                        }
                    }
                }
//                Iterator<Map.Entry<Integer, Map<Integer, List<Integer>>>> iterator1 = rMap.entrySet().iterator();

            }
        });
//        timer.start();
    }
}
