package com.app.classes;

/**
 * Created by root on 7/22/16.
 */
public class Weather {

    String day;
    float dayHigh;
    float dayLow;
    String currentCondition;
    String currentPlace;

    public String getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(String currentPlace) {
        this.currentPlace = currentPlace;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public float getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(float dayHigh) {
        this.dayHigh = dayHigh;
    }

    public float getDayLow() {
        return dayLow;
    }

    public void setDayLow(float dayLow) {
        this.dayLow = dayLow;
    }

    public String getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(String currentCondition) {
        this.currentCondition = currentCondition;
    }
}
