package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class States implements Serializable{

    String stateName;

    String cStateName;

    String fs;

    List<City> cities=new ArrayList<>();

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

    @Override
    public String toString() {
        String json="{" +
                "\"stateName\":\"" + stateName + '\"' +
                ", \"cStateName\":\"" + cStateName + '\"' +
                ", \"fs\":\"" + fs + '\"' +", \"cities\":[";
         StringBuilder builder=new StringBuilder(2048);
         for (City city: cities){
             builder.append(city.toString()+",");
         }
        String ct = builder.toString();
        String c=ct.substring(0,ct.length()-1);

        return json+c+"]}";
    }
}
