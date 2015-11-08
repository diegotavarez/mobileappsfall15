package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class CityDetailActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

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

        setTitle(city);
        location.setLatitude(intent.getDoubleExtra("lat", 0));
        location.setLongitude(intent.getDoubleExtra("lng", 0));
        tvCityName.setText(city);
        temperature = intent.getStringExtra("temperature");
        humidity = intent.getStringExtra("humidity");
        tempMin = intent.getStringExtra("temp_min");
        tempMax = intent.getStringExtra("temp_max");
        windSpeed = intent.getStringExtra("windSpeed");
        description = intent.getStringExtra("description");

        setUpMapIfNeeded();

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
                intentWeatherInformation.putExtra("city", city);
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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng cityPosition;
        cityPosition = new LatLng(location.getLatitude(),location.getLongitude());

        CameraPosition camera = new CameraPosition.Builder()
                .target(cityPosition).zoom(10).build();
        mMap.getUiSettings().setZoomControlsEnabled(true); // (+) (-) zoom control bar

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.addMarker(new MarkerOptions().position(cityPosition));
    }
}
