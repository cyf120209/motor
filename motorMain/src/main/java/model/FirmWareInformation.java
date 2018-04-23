package model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/28.
 */
public class FirmWareInformation implements Serializable{

    private String type;
    private int typeNum;
    private int majorNum;
    private int minorNum;
    private int patchNum;
    private int crc;

    public FirmWareInformation() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public int getMajorNum() {
        return majorNum;
    }

    public void setMajorNum(int majorNum) {
        this.majorNum = majorNum;
    }

    public int getMinorNum() {
        return minorNum;
    }

    public void setMinorNum(int minorNum) {
        this.minorNum = minorNum;
    }

    public int getPatchNum() {
        return patchNum;
    }

    public void setPatchNum(int patchNum) {
        this.patchNum = patchNum;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }
}
