package model;

import java.io.Serializable;

public class Conf implements Serializable{

    private String modelName;

    private FirmWareInformation firmWareInformation1;

    private FirmWareInformation firmWareInformation2;

    public Conf() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public FirmWareInformation getFirmWareInformation1() {
        return firmWareInformation1;
    }

    public void setFirmWareInformation1(FirmWareInformation firmWareInformation1) {
        this.firmWareInformation1 = firmWareInformation1;
    }

    public FirmWareInformation getFirmWareInformation2() {
        return firmWareInformation2;
    }

    public void setFirmWareInformation2(FirmWareInformation firmWareInformation2) {
        this.firmWareInformation2 = firmWareInformation2;
    }
}
