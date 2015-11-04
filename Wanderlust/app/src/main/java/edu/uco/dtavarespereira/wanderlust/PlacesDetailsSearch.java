package edu.uco.dtavarespereira.wanderlust;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dani on 11/3/15.
 */
public class PlacesDetailsSearch {

    private final static String TAG = "PlacesDetailsSearch";
    private static Location loc = new Location("");
    public static Location getLocation(){
        return loc;
    }

    public static ArrayList<String> getData(String data) throws JSONException{

        ArrayList<String> resultsReturn = new ArrayList<>();
        // System.out.println(data);
        JSONObject jObj = new JSONObject(data);
        JSONObject jArr = jObj.getJSONObject("result");
        //JSONObject jArrTemp = jArr.getJSONObject(0);

        JSONObject jArr1 = jArr.getJSONObject("geometry").getJSONObject("location");
        Double lat = jArr1.getDouble("lat");
        Double lng = jArr1.getDouble("lng");

//        JSONObject jArr2 = jArr.getJSONObject("opening_hours");
        // boolean open_now = jArr2.getBoolean("open_now"); //TODO show if place is open or closed
        // jArr2 = jArr.getJSONObject("opening_hours").getJSONObject("weekday_text"); //TODO show what time it works


        String formatted_address, formatted_phone_number, website, name;

        name = jArr.getString("name");
        resultsReturn.add(name);

        formatted_address = jArr.getString("formatted_address");
        resultsReturn.add(formatted_address);

        try{
            website = jArr.getString("website");
            resultsReturn.add(website);
        } catch(JSONException e){
            resultsReturn.add("...");
        }

        try{
            formatted_phone_number = jArr.getString("formatted_phone_number");
            resultsReturn.add(formatted_phone_number);
        } catch(JSONException e){
            resultsReturn.add("...");
        }


        loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        return resultsReturn;
    }
}
