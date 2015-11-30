package edu.uco.dtavarespereira.wanderlust.entity;

/**
 * Created by diegotavarez on 11/24/15.
 */
public class Forecast {
    public Forecast(String maximumTemperature, String minimumTemperature, String dayOfTheWeek, int id) {
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.dayOfTheWeek = dayOfTheWeek;
        this.id = id;
    }

    String maximumTemperature;
    String minimumTemperature;
    String dayOfTheWeek;
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(String minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

}
