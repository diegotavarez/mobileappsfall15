package edu.uco.dtavarespereira.wanderlust.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.R;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng cityPosition;

    private double lat, lon;
    private String namePlace;

    ArrayList<ArrayList<String>> placesNames;
    ArrayList<Location> locationArray = new ArrayList<>();
    ArrayList<LatLng> locationsLatLng = new ArrayList<>();
    String name, formatted_address, formatted_phone_number, website;
    Bundle args = new Bundle();
    int intent = 0;
    private PlacesNamesDialogFragment fragmentPlacesDialog = new PlacesNamesDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent().getIntExtra("button", 1);

        if(intent == 1) {

            lat = getIntent().getDoubleExtra("lat", 0);
            lon = getIntent().getDoubleExtra("lon", 0);
            try {
                namePlace = getIntent().getStringExtra("name");
            } catch (Exception e) {
                namePlace = "";
            }
            cityPosition = new LatLng(lat, lon);
        }
        else if(intent == 2) {

            locationArray = (ArrayList<Location>) getIntent().getSerializableExtra("locations");
            placesNames =  (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("places_details");

            for(int i = 0; i < locationArray.size(); i++)
            {
                locationsLatLng.add(new LatLng(locationArray.get(i).getLatitude(), locationArray.get(i).getLongitude()));
            }

        }

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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

        CameraPosition camera;

        if(intent == 1) {
            camera = new CameraPosition.Builder()
                    .target(cityPosition).zoom(15).build();

            mMap.getUiSettings().setZoomControlsEnabled(true); // (+) (-) zoom control bar
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
            mMap.addMarker(new MarkerOptions().position(cityPosition).title(namePlace));
        }
        else if(intent == 2)
        {
            camera = new CameraPosition.Builder()
                    .target(locationsLatLng.get(0)).zoom(15).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

            ArrayList<String> s;

            for(int i = 0; i < locationsLatLng.size(); i++)
            {
                s = placesNames.get(i);
                name = s.get(0);
                formatted_address = s.get(1);
                website = s.get(2);
                formatted_phone_number = s.get(3);

                mMap.addMarker(new MarkerOptions().position(locationsLatLng.get(i)).title(name).snippet(formatted_address +
                        "!" + formatted_phone_number + "!" + website));

                //     args.putString("name", name);
                //   args.putString("address", formatted_address);
                // args.putString("phone", formatted_phone_number);
                //args.putString("website", formatted_phone_number);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        args.putString("name", marker.getTitle().toString());
                        args.putString("information", marker.getSnippet().toString());

                        fragmentPlacesDialog.setArguments(args);
                        fragmentPlacesDialog.show(getFragmentManager(), "oi");
                        return true;
                    }
                });

                //   args = new Bundle();
                //     mMap
            }

        }

    }
}
