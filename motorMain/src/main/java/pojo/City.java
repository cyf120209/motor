package pojo;

import java.io.Serializable;

public class City implements Serializable{

    private String cityName;

    private String cCityName;

    private double lng;

    private double lat;

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

    @Override
    public String toString() {
        return "{" +
                "\"cityName\":\"" + cityName + '\"' +
                ", \"cCityName\":\"" + cCityName + '\"' +
                ", \"lng\":" + lng +
                ", \"lat\":" + lat +
                '}';
    }
}
