package com.michailstanev.simpletasker;

import java.util.ArrayList;

public class StaticConstants {
    static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    static final String FENCE_RECEIVER_ACTION = "com.example.awarenessdevelopersgoogle.TESTING_HEADPHONE" ;
    static String IN_LOCATION_FENCE_KEY = "IN_LOCATION_FENCE_KEY_LMAO";

    static double currentLat=0;
    static double currentLong=0;

    static double markerLat=0;
    static double markerLong=0;

    public static final int STATUS_IN = 0;
    public static final int STATUS_OUT = 1;
    public static final int STATUS_ENTERING = 2;
    public static final int STATUS_EXITING = 3;

    public static ArrayList listOfKeys = new ArrayList();

    public static void setCurrentLat(double currentLat) {
        StaticConstants.currentLat = currentLat;
    }

    public static void setCurrentLong(double currentLong) {
        StaticConstants.currentLong = currentLong;
    }

    public static void setMarkerLat(double markerLat) {
        StaticConstants.markerLat = markerLat;
    }

    public static void setMarkerLong(double markerLong) {
        StaticConstants.markerLong = markerLong;
    }

    public static double getCurrentLat() {
        return currentLat;
    }

    public static double getCurrentLong() {
        return currentLong;
    }

    public static double getMarkerLat() {
        return markerLat;
    }

    public static double getMarkerLong() {
        return markerLong;
    }

    public static void addToActiveKeys(String s){
        listOfKeys.add(s);
    }

    public static void removeFromActiveKeys(String s){
        listOfKeys.remove(s);
    }
}
