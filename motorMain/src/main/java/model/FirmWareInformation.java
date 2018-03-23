package model;

/**
 * Created by lenovo on 2017/9/28.
 */
public class FirmWareInformation {

    private String type;
    private int typeNum;
    private String lastModify;
    private int majorNum;
    private int minorNum;
    private int patchNum;

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

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
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
}
