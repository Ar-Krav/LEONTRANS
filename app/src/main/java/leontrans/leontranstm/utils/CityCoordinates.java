package leontrans.leontranstm.utils;

public class CityCoordinates {
    private String lat;
    private String lng;

    public CityCoordinates(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
