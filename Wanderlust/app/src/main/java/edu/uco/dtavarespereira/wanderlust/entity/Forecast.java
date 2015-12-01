package edu.uco.dtavarespereira.wanderlust.entity;

/**
 * Created by diegotavarez on 11/24/15.
 */
public class Forecast {
    public Forecast(String maximumTemperature, String minimumTemperature, String dayOfTheWeek, String icon) {
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.dayOfTheWeek = dayOfTheWeek;
        this.icon = icon;
    }

    String maximumTemperature;
    String minimumTemperature;
    String dayOfTheWeek;
    String icon;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
