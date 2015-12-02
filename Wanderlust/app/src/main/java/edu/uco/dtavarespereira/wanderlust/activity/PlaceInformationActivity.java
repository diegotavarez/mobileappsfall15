package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.PlacesSearch;
import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.entity.Place;

public class PlaceInformationActivity extends Activity{
    Button button;
    TextView nameView, addressView, websiteView, phoneView;
    RatingBar ratingBar;
    String name, formatted_address, formatted_phone_number, website, category;
    double rating;
    int position;
    ArrayList<String> ids;

    ImageView img;
    Bitmap image;
    String photos;
    Switch swFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        final Double lat, lng;

        ids = PlacesSearch.getIds();
        img = (ImageView) findViewById(R.id.imageView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        swFavorite = (Switch) findViewById(R.id.sw_favorite);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        category = intent.getStringExtra("category");
        setTitle(category);

        formatted_address = intent.getStringExtra("address");
        website = intent.getStringExtra("website");
        formatted_phone_number = intent.getStringExtra("phone");
        position = intent.getIntExtra("position", 0);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        photos = intent.getStringExtra("photos");
        rating = Double.valueOf((intent.getStringExtra("rating")));
        ratingBar.setRating((float) rating);

        swFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Location location = new Location("");
                    Place place = new Place(null, "", name, formatted_address, formatted_phone_number, website, String.valueOf(rating),location, category);
                    InitialActivity.getDBHelper().addPlace(place);
                } else {

                }
            }
        });

        nameView = (TextView) findViewById(R.id.nameView);
        addressView = (TextView) findViewById(R.id.address);
        websiteView = (TextView) findViewById(R.id.website);
        phoneView = (TextView) findViewById(R.id.phone);

        nameView.setText("\n " + name + "\n ");
        if(name.length() > 28){
            nameView.setTextSize(18);
        }
        else nameView.setTextSize(24);
        addressView.setText("\n " + formatted_address + "\n ");
        websiteView.setText("\n " + website);
        phoneView.setText("\n " + formatted_phone_number);

        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberToDial = "tel:"+ formatted_phone_number;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(numberToDial)));
            }
        });

        websiteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(PlaceInformationActivity.this, WebBrowser.class);
                intent2.putExtra("url", website);
                startActivity(intent2);
            }
        });
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaps = new Intent(PlaceInformationActivity.this, MapsActivity.class);
                intentMaps.putExtra("lat", lat);
                intentMaps.putExtra("lon", lng);
                intentMaps.putExtra("name", name);
                startActivity(intentMaps);
            }
        });


        //placePhotosAsync();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if(photos.equals("empty"));
                else {
                    try {
                        InputStream in = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&" +
                                "photoreference=" + photos +
                                "&key=AIzaSyB6b7FiH5aq907kpEril4Q_DSWsEDhfeTs").openStream();
                        image = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        // log error
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (image != null){
                    img.setImageBitmap(image);
                }
            }

        }.execute();
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

}
