package update;

import com.serotonin.bacnet4j.RemoteDevice;
import model.FirmWareInformation;

public interface IUpgrade {

    public FirmWareInformation chooseFirmware(String path);

    public void startUpdate();

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
     * 设备列表及版本显示
     * @param device 设备列表
     */
    void addJListDevice(RemoteDevice device);

    /**
     * 找齐需要升级的所有设备
     */
    void findOriginDevice(int flag);
}
