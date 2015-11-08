package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaceInformation extends Activity {
    Button button;
    TextView nameView, addressView, websiteView, phoneView;
    String name, formatted_address, formatted_phone_number, website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        final Double lat, lng;

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        formatted_address = intent.getStringExtra("address");
       website = intent.getStringExtra("website");
        formatted_phone_number = intent.getStringExtra("phone");
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);

        nameView = (TextView) findViewById(R.id.nameView);
        addressView = (TextView) findViewById(R.id.address);
        websiteView = (TextView) findViewById(R.id.website);
        phoneView = (TextView) findViewById(R.id.phone);

        nameView.setText(name);
        addressView.setText(formatted_address);
        websiteView.setText(website);
        phoneView.setText(formatted_phone_number);

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
}
