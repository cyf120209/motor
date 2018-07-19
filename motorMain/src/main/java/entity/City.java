package entity;

import java.io.Serializable;

public class City implements Serializable{

    private int id;

    private int stateId;

    private String cityName;

    private String cCityName;

    private double lng;

    private double lat;

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getcCityName() {
        return cCityName;
    }

    public void setcCityName(String cCityName) {
        this.cCityName = cCityName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

}
