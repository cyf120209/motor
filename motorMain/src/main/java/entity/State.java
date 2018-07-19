package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable{

    private int stateId;

    private String stateName;

    private String cStateName;

    private String fs;

    private String country;

    List<City> cities=new ArrayList<>();

    public State() {
    }

    public State(String stateName, String cStateName, String fs, String country) {
        this.stateName = stateName;
        this.cStateName = cStateName;
        this.fs = fs;
        this.country = country;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getcStateName() {
        return cStateName;
    }

    public void setcStateName(String cStateName) {
        this.cStateName = cStateName;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
