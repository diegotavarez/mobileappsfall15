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
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        location = new Location("");
        location.setLatitude(51.503186);
        location.setLongitude(-0.126446); //TODO ~ get the location lat and lng of the city and set in this variable

        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        swFavorite = (Switch) findViewById(R.id.sw_favorite);

        final Intent intent = getIntent();
        city = intent.getStringExtra("CITY_NAME");
        tvCityName.setText(city);

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
