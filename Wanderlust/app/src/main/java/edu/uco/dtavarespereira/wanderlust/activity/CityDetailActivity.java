package edu.uco.dtavarespereira.wanderlust.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.R;

public class CityDetailActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    TextView tvCityName;
    Switch swFavorite;
    String city;
    Button placesToVisit;
    Button btnCommercialPlaces;
    Button btWeatherCondition;

    Location location;
    Location mLastLocation;
    LocationListener locationListener;
    protected GoogleApiClient mGoogleApiClient;


    String temperature, humidity, tempMin, tempMax, windSpeed, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws  SecurityException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        location = new Location("");
        mLastLocation = new Location("");
        buildGoogleApiClient();

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

        Toast.makeText(getApplicationContext(),"LAT: "+ mLastLocation.getLatitude(),Toast.LENGTH_SHORT).show();

        CameraPosition camera = new CameraPosition.Builder()
                .target(cityPosition).zoom(10).build();
        mMap.getUiSettings().setZoomControlsEnabled(true); // (+) (-) zoom control bar

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.addMarker(new MarkerOptions().position(cityPosition));
        mMap.addMarker(new MarkerOptions().position(currentPosition));
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
}

//class MyOverlay extends Overlay {
//
//    public MyOverlay() {
//
//    }
//
//    public void draw(Canvas canvas, MapView mapv, boolean shadow) {
//        super.draw(canvas, mapv, shadow);
//
//        Paint mPaint = new Paint();
//        mPaint.setDither(true);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(2);
//
//        Barcode.GeoPoint gP1 = new Barcode.GeoPoint(19240000, -99120000);
//        GeoPoint gP2 = new GeoPoint(37423157, -122085008);
//
//        Point p1 = new Point();
//        Point p2 = new Point();
//        Path path = new Path();
//
//        Projection projection = mapv.getProjection();
//        projection.toPixels(gP1, p1);
//        projection.toPixels(gP2, p2);
//
//        path.moveTo(p2.x, p2.y);
//        path.lineTo(p1.x, p1.y);
//
//        canvas.drawPath(path, mPaint);
//    }
//}
