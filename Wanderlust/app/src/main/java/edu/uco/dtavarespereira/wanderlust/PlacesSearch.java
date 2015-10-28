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
    public static Location getLocation(){
        return loc;
    }
    public static ArrayList<String> getData(String data) throws JSONException {

        JSONObject jObj = new JSONObject(data);
        JSONArray jArr = jObj.getJSONArray("results");
        JSONObject jArr1 = jArr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        Double lat = jArr1.getDouble("lat");
        Double lng = jArr1.getDouble("lng");

        ArrayList<String> resultsReturn = new ArrayList<>();
        String id, place_id, place_reference;

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
