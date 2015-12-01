package edu.uco.dtavarespereira.wanderlust.entity;

import android.location.Location;

/**
 * Created by Dani on 11/24/15.
 */
public class Place {

    public Place(){
        super();
    }

    public Place(final Integer DBID, String googleId, String name, String address, String phoneNumber, String website, String rating, Location location, String category) {
        this.DBID = DBID;
        this.googleID = googleId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.rating = rating;
        this.location = location;
        this.category = category;
    }

    private Integer DBID;
    private String googleID;
    private String name;
    private String address;
    private String phoneNumber;
    private String website;
    private String rating;
    private Location location;
    private String category;

    public Integer getDBID() {
        return DBID;
    }

    public void setDBID(Integer DBID) {
        this.DBID = DBID;
    }
    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String id) {
        this.googleID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
