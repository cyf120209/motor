package update.presenter;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * Created by lenovo on 2017/1/20.
 */
public interface UpdatePresenter {

    void choosebt();

    void choosebt2();

    /**
     * 升级选中的电机
     */
    void updateToSelectButton();

    /**
     * 升级所有的电机
     */
    void updateButton();

    /**
     * 电机升级的状态
     * @return true：数据发送完， false：数据还在发送中
     */
    boolean getUpdateState();

    byte[] getfileTmp();

    /**
     * 获取固件type类型
     * @return
     */
    String getFirmWareType();

    /**
     * 升级失败回调
     * @param vendorId
     * @param serviceNumber
     * @param serviceParameters
     * @param address
     * @return
     */
    AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address);

    /**
     * 设备列表及版本显示
     * @param device 设备列表
     */
    void addJListDevice(RemoteDevice device);

    /**
     * 设备原始列表显示
     * @param device
     */
    void addJListDeviceOrigin(RemoteDevice device);

    /**
     * 设备升级前列表显示
     * @param device
     */
    void addJListDeviceBefore(RemoteDevice device);

    /**
     * 设备升级后列表显示
     * @param device
     */
    void addJListDeviceAfter(RemoteDevice device);

    /**
     * 用户确认更新
     * @param isCancel
     */
    void update(boolean isCancel);

    /**
     * 注销监听
     */
    void cancelListener();

    /**
     * 获取升级状态
     * @return
     */
    int getFlag();

    /**
     * 通知找齐升级前的所有设备
     */
    void findBeforeDevice();

    /**
     * 找齐需要升级的所有设备
     */
    void findOriginDevice(int flag);

    /**
     * 取消升级
     */
    void cancelUpgrade();

    int getAbnormalRemoteDeviceSize();
    void autoUpdate1();
    void autoUpdate2();
    void autoOneUpdate1();
    void autoOneUpdate2();
}
