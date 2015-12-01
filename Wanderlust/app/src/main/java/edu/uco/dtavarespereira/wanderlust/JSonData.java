package edu.uco.dtavarespereira.wanderlust;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

            /*JSONObject coordObj = forecast.getJSONObject("coord");
            result.add(Double.toString(coordObj.getDouble("lat")));//0
            result.add(Double.toString(coordObj.getDouble("lon")));//1

            JSONObject tempObj = forecast.getJSONObject("main");
            result.add(tempObj.getString("temp"));//2
            result.add(tempObj.getString("humidity"));//3
            result.add(tempObj.getString("temp_min"));//4
            result.add(tempObj.getString("temp_max"));//5

            JSONObject windObj = forecast.getJSONObject("wind");
            result.add(windObj.getString("speed"));//6

            JSONArray descObj = forecast.getJSONArray("weather");
	    result.add(descObj.getJSONObject(0).getString("id"));//7
            result.add(descObj.getJSONObject(0).getString("description"));//8*/


            JSONArray weekArray = forecast.getJSONArray("list");

            JSONObject cityJson = forecast.getJSONObject("city");
            String city = cityJson.getString("name"); // city name



            JSONObject cityCoord = cityJson.getJSONObject("coord"); // coordinate
            double lat = cityCoord.getDouble("lat"); //latitude
            double lon = cityCoord.getDouble("lon"); // longitude

            result.add(city + "/" + lat + "/" + lon);

            for (int i = 0; i < weekArray.length(); i++) {

                // Get the JSON object representing the day
                JSONObject dayForecast = weekArray.getJSONObject(i);

                int humidity = dayForecast.getInt("humidity");
                double windSpeed = dayForecast.getDouble("speed");

                JSONObject weatherObject = dayForecast.getJSONArray("weather").getJSONObject(0);
                String icon = weatherObject.getString("icon");
                String description = weatherObject.getString("description");

                // Temperatures are in a child object called "temp".
                JSONObject temperatureObject = dayForecast.getJSONObject("temp");
                double temp = temperatureObject.getDouble("day"); // temperature
                double high = temperatureObject.getDouble("max"); // max temperature
                double low = temperatureObject.getDouble("min"); // min temperature

                long timestamp = dayForecast.getLong("dt");

                result.add(icon + "/" + description + "/" + temp + "/" + high + "/" + low + "/" + humidity + "/" + windSpeed + "/" + timestamp);
            }



            return result;




        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
}
