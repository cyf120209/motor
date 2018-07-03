package pojo;

import java.math.BigDecimal;
import java.util.List;

public class Cities {


    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public class City {
        /**
         * cityName :  Ashby
         * state :  AL (US)
         * lng : 33.0166666666667
         * lat : 86.9166666666667
         */

        private String cityName;
        private String state;
        private double lng;
        private double lat;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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
            return "{\"cityName\":\"" +
                    cityName.trim()+"\",\"state\":\""+
                    state.trim()+"\",\"lng\":"+
                    -getDouble3(lat)+",\"lat\":"+getDouble3(lng)+
                    "}";
        }

        private double getDouble3(double num){
            return new BigDecimal(num).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }
}
