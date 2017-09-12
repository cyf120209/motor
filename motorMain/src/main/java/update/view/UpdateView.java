package update.view;

import com.serotonin.bacnet4j.RemoteDevice;

import java.util.List;

/**
 * Created by lenovo on 2017/1/20.
 */
public interface UpdateView {

    void updateFileText(String text);

    void updateTypeLabelText(String text);

    String getFileName();

    int getMajorNum();

    int getMinorNum();

    int getPatchNum();

    int getTypeNum();

    RemoteDevice getdevBoxSelectedItem();

    void updateVersionLabel(String text);

    void updateDevBox(List<RemoteDevice> remoteDevices);

    void updateDevBox(RemoteDevice remoteDevice);

    void updateVersionAndType(String type,String major,String minor,String patch);

    void showError(String str);

    /**
     * 更新进度
     * @param per
     */
    void updateProgress(int per);

    /**
     * 获取版本号
     * @param version
     */
    void showOriginalDeviceVersion(String version);

    /**
     * 更新之前的版本号
     * @param version
     */
    void showBeforeDeviceVersion(String version);

    /**
     * 更新之后的版本号
     * @param version
     */
    void showAfterDeviceVersion(String version);

    /**
     * 显示是否升级对话框
     */
    void showConfirmDialog();

    /**
     * 显示固件最后修改日期
     * @param lastModify
     */
    void updateLastModify(String lastModify);

    /**
     * 升级的电机数量
     * @return
     */
    int getOriginalSize();

    /**
     * 升级前的单机数量
     * @return
     */
    int getBeforeSize();


}
