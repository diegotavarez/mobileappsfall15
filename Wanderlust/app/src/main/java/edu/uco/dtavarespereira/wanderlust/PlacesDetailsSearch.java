package edu.uco.dtavarespereira.wanderlust;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import edu.uco.dtavarespereira.wanderlust.activity.Place;

/**
 * Created by Dani on 11/3/15.
 */
public class PlacesDetailsSearch {

    private final static String TAG = "PlacesDetailsSearch";
    private static Location loc = new Location("");
    public static Location getLocation(){
        return loc;
    }
    private static String photo;
    public static String getPhoto(){
        return photo;
    }
    public static ArrayList<Place> placeArray = new ArrayList<>();

    public static void orderByName(){
        Collections.sort(placeArray, new Comparator<Place>() {
            @Override public int compare(Place p1, Place p2) {
                return p1.getName().compareTo(p2.getName()); // Ascending
            }

        });
    }

    public static void orderByLowerRating(){
        Collections.sort(placeArray, new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p1.getRating().compareTo(p2.getRating()); // Ascending
            }

        });

    }

    public static void orderByHigherRating(){
        Collections.sort(placeArray, new Comparator<Place>() {
            @Override
            public int compare(Place p1, Place p2) {
                return p1.getRating().compareTo(p2.getRating()); // Ascending
            }

        });
        Collections.reverse(placeArray);

    }

    public static ArrayList<String> getData(String data) throws JSONException{

        Place placeObject = new Place();
        ArrayList<String> resultsReturn = new ArrayList<>();
        // System.out.println(data);
        JSONObject jObj = new JSONObject(data);
        JSONObject jArr = jObj.getJSONObject("result");
        //JSONObject jArrTemp = jArr.getJSONObject(0);

        try {
            JSONArray jPhotos = jArr.getJSONArray("photos");
            JSONObject firstPhoto = jPhotos.getJSONObject(0);
            photo = firstPhoto.getString("photo_reference");
        } catch(JSONException e){
                photo = "empty";
            }

        JSONObject jArr1 = jArr.getJSONObject("geometry").getJSONObject("location");
        Double lat = jArr1.getDouble("lat");
        Double lng = jArr1.getDouble("lng");

//        JSONObject jArr2 = jArr.getJSONObject("opening_hours");
        // boolean open_now = jArr2.getBoolean("open_now"); //TODO show if place is open or closed
        // jArr2 = jArr.getJSONObject("opening_hours").getJSONObject("weekday_text"); //TODO show what time it works


        String formatted_address, formatted_phone_number, website, name, rating;

        name = jArr.getString("name");
        resultsReturn.add(name);
        placeObject.setName(name);

        formatted_address = jArr.getString("formatted_address");
        resultsReturn.add(formatted_address);
        placeObject.setAddress(formatted_address);

        try{
            website = jArr.getString("website");
            resultsReturn.add(website);
            placeObject.setWebsite(website);
        } catch(JSONException e){
            resultsReturn.add("");
            placeObject.setWebsite("");
        }

        try{
            formatted_phone_number = jArr.getString("formatted_phone_number");
            resultsReturn.add(formatted_phone_number);
            placeObject.setPhoneNumber(formatted_phone_number);
        } catch(JSONException e){
            resultsReturn.add("");
            placeObject.setPhoneNumber("");
        }

        try {
            rating = String.valueOf(jArr.getDouble("rating"));
            resultsReturn.add(rating);
            placeObject.setRating(rating);
        } catch(JSONException e){
                resultsReturn.add("0");
                placeObject.setRating("0");
            }

        loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        placeObject.setLocation(loc);

        placeArray.add(placeObject);
        return resultsReturn;
    }
}
