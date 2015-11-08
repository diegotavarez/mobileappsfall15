package edu.uco.dtavarespereira.wanderlust;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dani on 10/27/15.
 */
public class PlacesSearch {

    private static Location loc = new Location("");
    private static int resultLenght = 0;
    public static Location getLocation(){
        return loc;
    }
    public static int getLenght(){
        return resultLenght;
    }

    public static ArrayList<String> getData(String data, int pos) throws JSONException {
        //System.out.println("search" + data);
        JSONObject jObj = new JSONObject(data);
        JSONArray jArr = jObj.getJSONArray("results");
        JSONObject jArr1 = jArr.getJSONObject(pos).getJSONObject("geometry").getJSONObject("location");
        resultLenght = jArr.length();
        Double lat = jArr1.getDouble("lat");
        Double lng = jArr1.getDouble("lng");

        ArrayList<String> resultsReturn = new ArrayList<>();
        String id, place_id, place_reference;

        /* TODO break array result into parts - jArr.length()*/
        int k = jArr.length();
        if(k == 0)
            resultsReturn.add("No result found!");
        else if(k > 0) {
            if(k > 20)
                k = 20;

            for (int i = 0; i < k; i++) {
                JSONObject jArrTemp = jArr.getJSONObject(i);
                String place_idS = jArrTemp.getString("place_id");
                resultsReturn.add(place_idS);
            }
        }
        //    JSONObject Objloc = jArr2.getJSONObject("location");
        loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        //   id = jArr2.getString("id");
        //   resultsReturn.add(id);

        // JSONObject ObjPlaceId = jArr.getJSONObject(1);
        // place_id = jArr2.getString("place_id");
        // resultsReturn.add("place_id");

        //  JSONObject ObjPlace_Reference = jArr.getJSONObject(1);
        //  place_reference = jArr2.getString("reference");
        //  resultsReturn.add("place_reference");


        return resultsReturn;
    }
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

}
