package suntracking.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import listener.SuntrackingListener;
import model.DeviceGroup;
import model.ShadeParameter;
import model.ShutterParameter;
import pojo.LngLat;
import suntracking.SunTrackingManager;
import suntracking.view.ISuntrackingView;
import util.Draper;
import util.DraperSubItem;
import util.DraperSubList;
import util.MyLocalDevice;

import java.util.*;

public class SuntrackingPresenterImpl implements SuntrackingPresenter {

    private final LocalDevice localDevice;
    private SuntrackingListener mSuntrackingListener;
    private ISuntrackingView mSuntrackingView;
    private LngLat lngLat;
    private ShadeParameter shadeParameter;
    private ShutterParameter shutterParameter;

    /**
     * 设备和组和电机的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();


    @Override
    public LngLat getLngLat() {
        return lngLat;
    }

    @Override
    public ShadeParameter getShadeParameter() {
        return shadeParameter;
    }

    @Override
    public ShutterParameter getShutterParameter() {
        return shutterParameter;
    }

    @Override
    public List<DeviceGroup> getDeviceGroup() {
        return mSuntrackingView.getDeviceGroup();
    }

    public SuntrackingPresenterImpl(ISuntrackingView suntrackingView) {
        this.mSuntrackingView=suntrackingView;
        SunTrackingManager.init(this);
        mSuntrackingListener = new SuntrackingListener(this);
        localDevice = MyLocalDevice.getInstance();
        localDevice.getEventHandler().addListener(mSuntrackingListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    sendAnnounce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void setLngLat(LngLat lngLat) {
        this.lngLat=lngLat;
    }

    @Override
    public void setShadeParameter(ShadeParameter shadeParameter) {
        this.shadeParameter=shadeParameter;
    }

    @Override
    public void setShutterParameter(ShutterParameter shutterParameter) {
        this.shutterParameter=shutterParameter;
    }

    @Override
    public void sendAnnounce() {
        try {
            Draper.sendAnnounce();
            mSequence.clear();
            mRelativeList.clear();
            mDevicesIDList.clear();
            System.out.println("send Anounce");
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    /**
     * announce类型 1：代表全部，2：代表组
     */
    private int mAnnounceType = 1;

    @Override
    public synchronized void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters) {
        Sequence parms = serviceParameters;

        if (serviceNumber.intValue() == 7) {
            if (mAnnounceType == 1) {
                deviceAnnounce(parms);
            } else {
                groupAnnounce(parms);
            }
        }
    }

    private void groupAnnounce(Sequence parms) {
        Map<String, Encodable> values1 = parms.getValues();
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        List<Integer> list = new ArrayList<>();
        list.add(instanceNumber1);
    }

    /**
     * serviceParameters参数列表，主要防止重复添加
     */
    private List<Sequence> mSequence = new ArrayList<>();

    private void deviceAnnounce(Sequence parms) {
        //parms 去重处理
        if (mSequence.contains(parms)) {
            return;
        }
        mSequence.add(parms);
        Map<String, Encodable> values1 = parms.getValues();
        //获取draperID
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        //该draperID下的设备-组关系
        DraperSubList deviceGroup1 = (DraperSubList) values1.get("DeviceGroup");
        for (DraperSubItem item1 : deviceGroup1.getList()) {
            Map<Integer, List<Integer>> CdevGrpInf = null;
            CdevGrpInf = mRelativeList.get(item1.getDevicID().getInstanceNumber());
            if (CdevGrpInf == null) {
                CdevGrpInf = new HashMap<Integer, List<Integer>>();
                mRelativeList.put(item1.getDevicID().getInstanceNumber(), CdevGrpInf);
            }
            if (!mDevicesIDList.contains(item1.getDevicID().getInstanceNumber())) {
                mDevicesIDList.add(item1.getDevicID().getInstanceNumber());
            }
            List<Integer> devList = null;
            devList = CdevGrpInf.get(item1.getGroupID().intValue());
            if (devList == null) {
                devList = new LinkedList<Integer>();
                CdevGrpInf.put(item1.getGroupID().intValue(), devList);
            }
            if (!devList.contains(instanceNumber1)) {
                devList.add(instanceNumber1);
            }
        }
        MyLocalDevice.mRemoteUtils.setRelationMap(mRelativeList);
        mSuntrackingView.updateExistedGroup(mDevicesIDList.toArray());
    }

}
