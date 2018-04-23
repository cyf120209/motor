package update;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import manager.rmi.IUpgradeCallback;
import model.FirmWareInformation;
import model.FirmWareResult;

import java.io.File;
import java.util.List;

public interface IUpgrade {

    public FirmWareResult chooseFirmware(String path);

    List<FirmWareInformation> chooseFirmware(File file);

    public void startUpdate(IUpgradeCallback callback);

    public int getUpgradeProgress();

    /**
     * 电机升级的状态
     * @return true：数据发送完， false：数据还在发送中
     */
    boolean getUpdateState();

    /**
     * 获取升级状态
     * @return
     */
    int getFlag();

    /**
     * 获取固件type类型
     * @return
     */
    String getFirmWareType();

    /**
     * 获取固件type类型
     * @return
     */
    String getFirmWareModelName();

    /**
     * 设备列表及版本显示
     * @param device 设备列表
     */
    void addJListDevice(RemoteDevice device);

    /**
     * 找齐需要升级的所有设备
     */
    void findOriginDevice(int flag);

    /**
     * 升级失败回调
     * @param vendorId
     * @param serviceNumber
     * @param serviceParameters
     * @param address
     * @return
     */
    AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address);

}
