package com.example.dmiprojekt4;

public class Station {


    String stationID;
    String stationName;
    String lngLat;
    String height;
    String setup;


    public Station(String stationID, String stationName, String lngLat, String height, String setup) {

        this.stationID = stationID;
        this.stationName = stationName;
        this.lngLat = lngLat;
        this.height = height;
        this.setup = setup;
    }

    @Override
    public String toString() {
        return stationName;
    }

    public String getStationID() {
        return stationID;
    }


}
