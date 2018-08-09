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

    private String mac;

    private String modelName;

    private String version;

    private String remarks;

    private Integer isShow;

//    private Set shadeGroups=new HashSet();
    private List<ShadeGroup> shadeGroups =new ArrayList<ShadeGroup>();


    public Device() {
        super();
    }

    public Device(Integer deviceId, String mac) {
        this.deviceId = deviceId;
        this.mac = mac;
    }

    public Device(Integer deviceId , String mac, String modelName, String version) {
        this(deviceId,"",mac,modelName,version,"");
    }

    public Device(Integer deviceId, String deviceName, String mac, String modelName, String version) {
        this(deviceId,deviceName,mac,modelName,version,"");
    }

    public Device(Integer deviceId, String deviceName, String mac, String modelName, String version, String remarks) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.mac = mac;
        this.modelName = modelName;
        this.version=version;
        this.remarks=remarks;
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

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    //    public Set getShadeGroups() {
//        return shadeGroups;
//    }
//
//    public void setShadeGroups(Set shadeGroups) {
//        this.shadeGroups = shadeGroups;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;

        Device device = (Device) o;

        if (deviceId != null ? !deviceId.equals(device.deviceId) : device.deviceId != null) return false;
        if (mac != null ? !mac.equals(device.mac) : device.mac != null) return false;
        if (modelName != null ? !modelName.equals(device.modelName) : device.modelName != null) return false;
        return version != null ? version.equals(device.version) : device.version == null;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
