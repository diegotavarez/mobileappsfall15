package edu.uco.dtavarespereira.wanderlust;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Danilo on 10/26/2015.
 */
public class JSonData {

    private static final String TAG = "JSONWeatherData";


    public static ArrayList<String> getData(String forecastJsonStr)
            throws JSONException {


        try {

            ArrayList<String> result = new ArrayList<String>();

            JSONObject forecast = new JSONObject(forecastJsonStr);

            JSONObject coordObj = forecast.getJSONObject("coord");
            result.add(Double.toString(coordObj.getDouble("lat")));//0
            result.add(Double.toString(coordObj.getDouble("lon")));//1

            JSONObject tempObj = forecast.getJSONObject("main");
            result.add(tempObj.getString("temp"));//2

            JSONObject windObj = forecast.getJSONObject("wind");
            result.add(windObj.getString("speed"));//3

            JSONArray descObj = forecast.getJSONArray("weather");
            result.add(descObj.getJSONObject(0).getString("description"));//4


            return result;




        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
}