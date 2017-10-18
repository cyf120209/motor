package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/16.
 */
public class Device implements Serializable {

    private Integer id;

    private Integer deviceId;

    private String deviceName;

    private Integer devicePosition;

    private Integer devicePriority;

    private String deviceStatus;

    private String mac;

    private String modelName;

    private String version;

    private String remarks;

//    private Set shadeGroups=new HashSet();
    private List<ShadeGroup> shadeGroups =new ArrayList<ShadeGroup>();


    public Device() {
        super();
    }

    public Device(Integer deviceId, String deviceName, String mac, String modelName,String version) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.mac = mac;
        this.modelName = modelName;
        this.version=version;
    }

    public Device(Integer deviceId, String deviceName, Integer devicePosition, Integer devicePriority, String deviceStatus) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.devicePosition = devicePosition;
        this.devicePriority = devicePriority;
        this.deviceStatus = deviceStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDevicePosition() {
        return devicePosition;
    }

    public void setDevicePosition(Integer devicePosition) {
        this.devicePosition = devicePosition;
    }

    public Integer getDevicePriority() {
        return devicePriority;
    }

    public void setDevicePriority(Integer devicePriority) {
        this.devicePriority = devicePriority;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public List<ShadeGroup> getShadeGroups() {
        return shadeGroups;
    }

    public void setShadeGroups(List<ShadeGroup> shadeGroups) {
        this.shadeGroups = shadeGroups;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //    public Set getShadeGroups() {
//        return shadeGroups;
//    }
//
//    public void setShadeGroups(Set shadeGroups) {
//        this.shadeGroups = shadeGroups;
//    }
}
