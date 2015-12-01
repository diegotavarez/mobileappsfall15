package edu.uco.dtavarespereira.wanderlust.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.R;

public class CityDetailActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    TextView tvCityName, tvTemperature;
    Switch swFavorite;
    String city;
    Button placesToVisit;
    Button btnCommercialPlaces;
    Button btWeatherCondition;
    ImageView weatherIcon;
    ArrayList<String> result;

    Location location;
    Location mLastLocation;
    LocationListener locationListener;
    protected GoogleApiClient mGoogleApiClient;


    String temperature, humidity, tempMin, tempMax, windSpeed, description, icon;
    String[] name_lat_lon, weatherDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws  SecurityException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        location = new Location("");
        mLastLocation = new Location("");
        buildGoogleApiClient();

        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        swFavorite = (Switch) findViewById(R.id.sw_favorite);

        result = new ArrayList<String>();
        final Intent intent = getIntent();
        result = intent.getStringArrayListExtra("listDays");

        name_lat_lon = result.get(0).split("/");

        city = name_lat_lon[0];//intent.getStringExtra("CITY_NAME");
        //Toast.makeText(getApplication(),result.get(0), Toast.LENGTH_SHORT).show();


        setTitle(city);
        location.setLatitude(Double.parseDouble(name_lat_lon[1]));//lat   intent.getDoubleExtra("-97.478104", 0)
        location.setLongitude(Double.parseDouble(name_lat_lon[2]));//lng    intent.getDoubleExtra("35.652828", 0)
        tvCityName.setText(city);

        weatherDetails = result.get(1).split("/");

        temperature = weatherDetails[2];//intent.getStringExtra("temperature");
        humidity = weatherDetails[5];//intent.getStringExtra("humidity");
        tempMin = weatherDetails[4];//intent.getStringExtra("temp_min");
        tempMax = weatherDetails[3];//intent.getStringExtra("temp_max");
        windSpeed = weatherDetails[6];//intent.getStringExtra("windSpeed");
        description = weatherDetails[1];//intent.getStringExtra("description");
        icon = weatherDetails[0];//intent.getStringExtra("id");

        tvTemperature.setText(temperature.substring(0,temperature.indexOf(".")) + "˚ C");
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        WeatherInformationActivity.defineImage(icon, weatherIcon);

        ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");
        if(favorites.contains(capitalize(city))) {
            swFavorite.setChecked(true);
        }
            swFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");

                    if(!favorites.contains(capitalize(city))){
                        favorites.add(capitalize(city));
                    }

                    InitialActivity.setStringArrayPref(getApplicationContext(), "FAVORITES", favorites);

                    Toast.makeText(getApplicationContext(), city + " was marked as favorite.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");
                    ArrayList<String> newList = new ArrayList<String>();

                    for (int i = 0; i < favorites.size(); i++) {
                        if (!capitalize(favorites.get(i)).equals(capitalize(city))){
                            newList.add(capitalize(city));
                        }
                    }
                    InitialActivity.setStringArrayPref(getApplicationContext(), "FAVORITES", newList);

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

                Intent intentCommercialPlaces = new Intent(getApplication(), CommercialPlacesActivity.class);
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
                /*intentWeatherInformation.putExtra("city", city);
                intentWeatherInformation.putExtra("temp", temperature);
                intentWeatherInformation.putExtra("humidity", humidity);
                intentWeatherInformation.putExtra("tempMin", tempMin);
                intentWeatherInformation.putExtra("tempMax", tempMax);
                intentWeatherInformation.putExtra("wSpeed", windSpeed);
                intentWeatherInformation.putExtra("descrip", description);
                intentWeatherInformation.putExtra("id", id);*/
                intentWeatherInformation.putStringArrayListExtra("infoList", result);
                startActivity(intentWeatherInformation);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

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
        LatLng cityPosition = new LatLng(location.getLatitude(),location.getLongitude());
        LatLng currentPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        CameraPosition camera = new CameraPosition.Builder()
                .target(cityPosition).zoom(10).build();
        mMap.getUiSettings().setZoomControlsEnabled(true); // (+) (-) zoom control bar

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.addMarker(new MarkerOptions().position(cityPosition).title(city));
        mMap.addMarker(new MarkerOptions().position(currentPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Current Position"));

        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(currentPosition.latitude, currentPosition.longitude),
                        new LatLng(cityPosition.latitude, cityPosition.longitude))
                .width(5).color(Color.parseColor("#8800897B")).geodesic(true));
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        setUpMapIfNeeded();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onBackPressed()
    {
        CityDetailActivity.this.finish();

        ArrayList<String> favorites = InitialActivity.getStringArrayPref(getApplicationContext(), "FAVORITES");
        if (favorites.size() > 0) {
            Intent intentFav = new Intent(CityDetailActivity.this, FavoritesActivity.class);
            intentFav.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentFav);
        }
        else
        {
            Intent intentInitial = new Intent(CityDetailActivity.this, InitialActivity.class);
            intentInitial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentInitial);
        }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}


