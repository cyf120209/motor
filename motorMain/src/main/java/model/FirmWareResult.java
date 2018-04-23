package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FirmWareResult implements Serializable{

    private int code;

    private String message;

    List<FirmWareInformation> firmWareInformationList=new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FirmWareInformation> getFirmWareInformationList() {
        return firmWareInformationList;
    }

    public void setFirmWareInformationList(List<FirmWareInformation> firmWareInformationList) {
        this.firmWareInformationList = firmWareInformationList;
    }
}
