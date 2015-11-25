package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        imageDescription = (ImageView) findViewById(R.id.imageDescription);
        infoList = new ArrayList<String>();
        weekList = new ArrayList<String>();
        list = (ListView) findViewById(R.id.listView);
        List<Forecast> listForecast = new ArrayList<Forecast>();

        Intent intent = getIntent();
        infoList = intent.getStringArrayListExtra("infoList");

        name_coord = infoList.get(0).split("/");

        setTitle(name_coord[0]);//intent.getStringExtra("city")

        weather_details = infoList.get(1).split("/");

        for (int i = 1; i < 8; i++)
        {
            String maxTemperature = weather_details[3];
            String minTemperature = weather_details[4];
            long timestamp = Long.parseLong(weather_details[7]);
            int id = Integer.parseInt(weather_details[0]);

            Date date  = new Date(timestamp);
            DateFormat format = new SimpleDateFormat("EE");
            String dayOfTheWeek = format.format(date);

            Forecast forecast = new Forecast(maxTemperature,minTemperature,dayOfTheWeek,id);
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

    public static void defineImage(int id, ImageView imageView) {

        if (id >= 200 && id <= 232) {
            imageView.setImageResource(R.mipmap.thunderstorm);
        } else if (id >= 300 && id <= 321) {
            imageView.setImageResource(R.mipmap.shower_rain);
        } else if (id >= 500 && id <= 531) {
            imageView.setImageResource(R.mipmap.rain);
        } else if (id >= 600 && id <= 622) {
            imageView.setImageResource(R.mipmap.snow);
        } else if (id >= 701 && id <= 781) {
            imageView.setImageResource(R.mipmap.mist);
        } else if (id == 800) {
            imageView.setImageResource(R.mipmap.clear_sky);
        } else if (id == 801) {
            imageView.setImageResource(R.mipmap.few_clouds);
        } else if (id == 802) {
            imageView.setImageResource(R.mipmap.scattered_clouds);
        } else if (id == 803) {
            imageView.setImageResource(R.mipmap.broken_clouds);
        } else {
            imageView.setImageResource(R.mipmap.scattered_clouds);
        }

    }

}
