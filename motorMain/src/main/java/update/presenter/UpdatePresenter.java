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

//    int getMajorNum();
//
//    int getMinorNum();
//
//    int getPatchNum();
//
//    int getTypeNum();

    byte[] getfileTmp();

    String getFileName();

    /**
     * 读取版本号
     * @param remoteDevice
     * @return
     */
    String ReadVersion(RemoteDevice remoteDevice);

    void ReadValue();

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
     * 电机列表及版本显示
     * @param device
     */
    void addJListDevice(RemoteDevice device);

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
     * 通知找齐所有设备
     */
    void findAllDevice();
}
