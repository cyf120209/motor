package update.view;

import com.serotonin.bacnet4j.RemoteDevice;
import update.presenter.FirmWareInformation;

import java.util.List;

/**
 * Created by lenovo on 2017/1/20.
 */
public interface UpdateView {

    void updateFileText(String text);

    void updateFileText2(String text);

    int getDelay();

    String getFileName();

    RemoteDevice getdevBoxSelectedItem();

    void updateVersionLabel(String text);

    void updateDevBox(List<RemoteDevice> remoteDevices);

    void updateDevBox(RemoteDevice remoteDevice);

    void updateVersionAndType(FirmWareInformation firmWareInformation);

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
    void showConfirmDialog(String str);

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

    /**
     * 更新升级信息
     * @param version
     */
    void showUpgradeInformation(String version);
}
