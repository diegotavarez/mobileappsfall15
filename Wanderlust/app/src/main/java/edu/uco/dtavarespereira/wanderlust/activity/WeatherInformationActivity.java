package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.adapter.WeeklyForecastAdapter;
import edu.uco.dtavarespereira.wanderlust.entity.Forecast;

public class WeatherInformationActivity extends Activity {

    TextView description, temperature, humidity, tempMin, tempMax, windSpeed;
    private static ImageView imageDescription;
    static ArrayList<String> infoList, weekList;
    String[] name_coord, weather_details;
    static ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_information);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = findViewById(android.R.id.content);

        description = (TextView) findViewById(R.id.textViewDescription);
        temperature = (TextView) findViewById(R.id.textViewTemp);
        humidity = (TextView) findViewById(R.id.textViewHumidity);
        tempMin = (TextView) findViewById(R.id.textViewMin);
        tempMax = (TextView) findViewById(R.id.textViewMax);
        windSpeed = (TextView) findViewById(R.id.textViewWinSpeed);
        imageDescription = (ImageView) findViewById(R.id.weather_icon);
        infoList = new ArrayList<String>();
        weekList = new ArrayList<String>();
        list = (ListView) findViewById(R.id.listView);
        List<Forecast> listForecast = new ArrayList<Forecast>();

        Intent intent = getIntent();
        infoList = intent.getStringArrayListExtra("infoList");
        name_coord = infoList.get(0).split("/");

        setTitle(name_coord[0]);//intent.getStringExtra("city")

        weather_details = infoList.get(1).split("/");
        description.setText(weather_details[1]);
        temperature.setText(weather_details[2].substring(0, weather_details[2].indexOf(".")) + "\u00b0" + "C");
        tempMin.setText(weather_details[4].substring(0, weather_details[4].indexOf(".")) + "\u00b0" + "C");
        tempMax.setText(weather_details[3].substring(0, weather_details[3].indexOf(".")) + "\u00b0" + "C");
        humidity.setText(weather_details[5] + " %");
        windSpeed.setText(weather_details[6] + " mph");
        defineImage(weather_details[0], imageDescription);

        for (int i = 2; i < infoList.size(); i++) {
            weather_details = infoList.get(i).split("/");
            String maxTemperature = weather_details[3].substring(0, weather_details[3].indexOf("."));
            String minTemperature = weather_details[4].substring(0, weather_details[4].indexOf("."));
            //long timestamp = 1448874000;//Long.parseLong(weather_details[7]);
            String icon = weather_details[0];

            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            //Date date  = new Date(timestamp);
            DateFormat format = new SimpleDateFormat("EE");
            String dayOfTheWeek = format.format(Long.parseLong(weather_details[7]) * 1000);
            //Toast.makeText(getApplicationContext(), icon + i, Toast.LENGTH_SHORT).show();

            Forecast forecast = new Forecast(maxTemperature, minTemperature, dayOfTheWeek, icon);
            listForecast.add(forecast);
        }

        WeeklyForecastAdapter adapter = new WeeklyForecastAdapter(getBaseContext(), listForecast, rootView);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void defineImage(String icon, ImageView imageView) {

        if(icon.equals("01d")){
            imageView.setImageResource(R.mipmap.clear_sky_night);
        }else if(icon.equals("01n")){
            imageView.setImageResource(R.mipmap.clear_sky_night);
        }else if(icon.equals("02d")) {
            imageView.setImageResource(R.mipmap.few_clouds);
        }else if (icon.equals("02n")) {
            imageView.setImageResource(R.mipmap.few_clouds_night);
        }else if (icon.equals("03d") || icon.equals("03n")){
            imageView.setImageResource(R.mipmap.scattered_clouds);
        }else if(icon.equals("04d") || icon.equals("04n")){
            imageView.setImageResource(R.mipmap.broken_clouds);
        }else if(icon.equals("09d") || icon.equals("09n")){
            imageView.setImageResource(R.mipmap.shower_rain);
        }else if(icon.equals("10d")){
            imageView.setImageResource(R.mipmap.rain);
        } else if (icon.equals("10n")){
            imageView.setImageResource(R.mipmap.rain_night);
        } else if(icon.equals("11d") || icon.equals("11n")){
            imageView.setImageResource(R.mipmap.thunderstorm);
        }else if(icon.equals("13d") || icon.equals("13n")){
            imageView.setImageResource(R.mipmap.snow);
        }else if(icon.equals("50d") || icon.equals("50n")){
            imageView.setImageResource(R.mipmap.mist);
        }

    }

}
