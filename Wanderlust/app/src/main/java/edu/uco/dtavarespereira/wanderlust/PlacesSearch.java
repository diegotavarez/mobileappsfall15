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

    private static Location loc = null;
    public static Location getLocation(){
        return loc;
    }
    public static ArrayList<String> getData(String data) throws JSONException {

        JSONObject jObj = new JSONObject(data);
        JSONArray jArr = jObj.getJSONArray("results");
        JSONArray jArr1 = jArr.getJSONArray(0);
        ArrayList<String> resultsReturn = new ArrayList<>();
        String id, place_id, place_reference;

        JSONObject Objloc = getObject("location", jArr1.getJSONObject(0));
        loc = new Location("");
        loc.setLatitude(getFloat("lat", Objloc));
        loc.setLongitude(getFloat("lng", Objloc));

        JSONObject ObjId = jArr.getJSONObject(1);
        id = ObjId.getString("id");
        resultsReturn.add(id);

        JSONObject ObjPlaceId = jArr.getJSONObject(1);
        place_id = ObjPlaceId.getString("place_id");
        resultsReturn.add(place_id);

        JSONObject ObjPlace_Reference = jArr.getJSONObject(1);
        place_reference = ObjId.getString("reference");
        resultsReturn.add(place_reference);


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
