package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class PlaceInformation extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    Button button;
    TextView nameView, addressView, websiteView, phoneView;
    String name, formatted_address, formatted_phone_number, website;
    int position;
    ArrayList<String> ids;


    String TAG = "edu.uco.dtavarespereira.wanderlust.PlaceInformation";
    ImageView img;

    PlacePhotoMetadataBuffer photoMetadataBuffer;
    Bitmap image;
    CharSequence attribution;
    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        final Double lat, lng;


        //TODO changes were submitted here in these lines bellow - in case of crush: delete it
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        ids = PlacesSearch.getIds();
        img = (ImageView) findViewById(R.id.imageView);
        img.setImageBitmap(image);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        formatted_address = intent.getStringExtra("address");
       website = intent.getStringExtra("website");
        formatted_phone_number = intent.getStringExtra("phone");
        position = intent.getIntExtra("position", 0); //TODO added line
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);

        nameView = (TextView) findViewById(R.id.nameView);
        addressView = (TextView) findViewById(R.id.address);
        websiteView = (TextView) findViewById(R.id.website);
        phoneView = (TextView) findViewById(R.id.phone);

        nameView.setText("\n " + name + "\n ");
        addressView.setText("\n \n " + formatted_address + "\n ");
        websiteView.setText("\n " + website);
        phoneView.setText("\n \n " + formatted_phone_number);



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaps = new Intent(PlaceInformation.this, MapsActivity.class);
                intentMaps.putExtra("lat", lat);
                intentMaps.putExtra("lon", lng);
                intentMaps.putExtra("name", name);
                startActivity(intentMaps);
                 //TODO get lgn and lat to set map
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_information, menu);
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


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        onGetPicture();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onGetPicture(){
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, ids.get(position)).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(PlacePhotoMetadataResult placePhotoMetadataResult) {
                if (placePhotoMetadataResult.getStatus().isSuccess()) {
                    PlacePhotoMetadataBuffer photoMetadata = placePhotoMetadataResult.getPhotoMetadata();
                    int photoCount = photoMetadata.getCount();
                   // for (int i = 0; i<photoCount; i++) {
                        PlacePhotoMetadata placePhotoMetadata = photoMetadata.get(0);
                    // Get a full-size bitmap for the photo.
                    image = placePhotoMetadata.getPhoto(mGoogleApiClient).await()
                            .getBitmap();
                    // Get the attribution text.
                    CharSequence attribution = placePhotoMetadata.getAttributions();
                  //  }
                    photoMetadata.release();
                } else {
                    Log.e(TAG, "No photos returned");
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
