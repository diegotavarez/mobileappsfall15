package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CityDetailActivity extends Activity {

    TextView tvCityName;
    Switch swFavorite;
    String city;
    Button placesToVisit;
    Button btnCommercialPlaces;
    Button btWeatherCondition;

    Location location;

    String temperature, humidity, tempMin, tempMax, windSpeed, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        location = new Location("");

        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        swFavorite = (Switch) findViewById(R.id.sw_favorite);

        final Intent intent = getIntent();
        city = intent.getStringExtra("CITY_NAME");
        location.setLatitude(intent.getDoubleExtra("lat", 0));
        location.setLongitude(intent.getDoubleExtra("lng", 0));
        tvCityName.setText(city);
        temperature = intent.getStringExtra("temperature");
        humidity = intent.getStringExtra("humidity");
        tempMin = intent.getStringExtra("temp_min");
        tempMax = intent.getStringExtra("temp_max");
        windSpeed = intent.getStringExtra("windSpeed");
        description = intent.getStringExtra("description");

        swFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");
                    favorites.add(city);
                    InitialActivity.setStringArrayPref(getApplicationContext(), "FAVORITES", favorites);

                    Toast.makeText(getApplicationContext(), city + " was marked as favorite.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");
                    favorites.remove(city);
                    InitialActivity.setStringArrayPref(getApplicationContext(), "FAVORITES", favorites);

                    Toast.makeText(getApplicationContext(), city + " was removed from favorites.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        placesToVisit = (Button) findViewById(R.id.btnPlacesToVisit);

        btnCommercialPlaces = (Button) findViewById(R.id.btnCommercial_places);

        btWeatherCondition = (Button)findViewById(R.id.btnWeatherConditions);

        placesToVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent1 = new Intent(getApplication(), PlacesToVisit.class);
                    intent1.putExtra("cityName", city);
                intent1.putExtra("locationLatitude", location.getLatitude());
                    intent1.putExtra("locationLongitude", location.getLongitude());
                    startActivity(intent1);
            }
        });

        btnCommercialPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCommercialPlaces = new Intent(getApplication(), CommercialPlaces.class);
                intentCommercialPlaces.putExtra("cityName", city);
                intentCommercialPlaces.putExtra("locationLatitude", location.getLatitude());
                intentCommercialPlaces.putExtra("locationLongitude", location.getLongitude());
                startActivity(intentCommercialPlaces);

            }
        });

        btWeatherCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentWeatherInformation = new Intent(getApplication(), WeatherInformationActivity.class);
                intentWeatherInformation.putExtra("temp", temperature);
                intentWeatherInformation.putExtra("humidity", humidity);
                intentWeatherInformation.putExtra("tempMin", tempMin);
                intentWeatherInformation.putExtra("tempMax", tempMax);
                intentWeatherInformation.putExtra("wSpeed", windSpeed);
                intentWeatherInformation.putExtra("descrip", description);
                startActivity(intentWeatherInformation);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_detail, menu);
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
}
