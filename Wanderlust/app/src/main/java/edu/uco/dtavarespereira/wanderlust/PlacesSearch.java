package edu.uco.dtavarespereira.wanderlust;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Dani on 10/27/15.
 */
public class PlacesSearch {

    private static Location loc = new Location("");
    private static ArrayList<String> ids = new ArrayList<>();
    private static int resultLenght = 0;
    protected static ArrayList<String> resultsToOrder;
    protected static boolean bigEnoughToOrder = false;
    public static Location getLocation(){
        return loc;
    }
    public static int getLenght(){
        return resultLenght;
    }
    public static ArrayList getIds(){
        return ids;
    }

    public static ArrayList<String> getData(String data, int pos) throws JSONException {
        JSONObject jObj = new JSONObject(data);
        JSONArray jArr = jObj.getJSONArray("results");
        JSONObject jArr1 = jArr.getJSONObject(pos).getJSONObject("geometry").getJSONObject("location");
        resultLenght = jArr.length();
        Double lat = jArr1.getDouble("lat");
        Double lng = jArr1.getDouble("lng");

        ArrayList<String> resultsReturn = new ArrayList<>();
        resultsToOrder = new ArrayList<>();

        int k = jArr.length();
        if(k == 0){
            resultsReturn.add("No result found!");
            bigEnoughToOrder = false;
        }
        else if(k > 0) {
            if(k > 20){
                k = 20;
                bigEnoughToOrder = true;
            }
            else {bigEnoughToOrder = false;}

            for (int i = 0; i < k; i++) {
                JSONObject jArrTemp = jArr.getJSONObject(i);
                String place_idS = jArrTemp.getString("place_id");
                resultsReturn.add(place_idS);
                String idS = jArrTemp.getString("id");
                ids.add(idS);
            }
        }

        loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        return resultsReturn;
    }


}
