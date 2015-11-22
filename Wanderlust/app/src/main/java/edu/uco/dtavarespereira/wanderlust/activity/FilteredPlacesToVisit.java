package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.PlaceInformation;
import edu.uco.dtavarespereira.wanderlust.R;

public class FilteredPlacesToVisit extends Activity {
    ListView places;
    Button order;
    ArrayList<ArrayList<String>> placesNames;
    ArrayList<String> names, ids;
    ArrayList<Location> locationsArray;
    ArrayList<String> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_places_to_visit);

        places = (ListView) findViewById(R.id.places);
        order = (Button) findViewById(R.id.order);
      final Intent intent = getIntent();
        int j = intent.getIntExtra("size", 0);
        placesNames = new ArrayList<>();
        locationsArray = new ArrayList<>();
        ids = new ArrayList<>();
        photos = new ArrayList<>();

        for(int i = 0; i < j; i++){
            placesNames.add(i,intent.getStringArrayListExtra("data " + i));
            locationsArray.add(i, new Location(""));
            locationsArray.get(i).setLatitude(intent.getDoubleExtra("location lat " + i, 0));
            locationsArray.get(i).setLongitude(intent.getDoubleExtra("location lng " + i, 0));
        }

        ids = intent.getStringArrayListExtra("ids");
        photos = intent.getStringArrayListExtra("photos");

      names = new ArrayList<>();
        for(ArrayList places : placesNames){
            ArrayList<String> s = places;
            // for(String data : s){
            // String formatted_address, formatted_phone_number, website;
            names.add(s.get(0));
            //   formatted_address = places.get(1).toString();
            //  website = places.get(2).toString();
            // formatted_phone_number = places.get(3).toString();
            // }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        places.setAdapter(adapter);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordering dialogFragment = new Ordering();
                dialogFragment.setRetainInstance(true);
                dialogFragment.show(getFragmentManager(), "tag");
            }
        });

        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), " it's working", Toast.LENGTH_SHORT).show();
                Intent intentFiltered = new Intent(FilteredPlacesToVisit.this, PlaceInformation.class);

                String name, formatted_address, formatted_phone_number, website, rating;
                ArrayList<String> s = placesNames.get(position);
                name = s.get(0);
                formatted_address = s.get(1);
                   website = s.get(2);
                   formatted_phone_number = s.get(3);
                rating = s.get(4);
                intentFiltered.putExtra("name",name);
                intentFiltered.putExtra("address",formatted_address);
                intentFiltered.putExtra("website",website);
                intentFiltered.putExtra("phone",formatted_phone_number);
                intentFiltered.putExtra("rating", rating);
                intentFiltered.putExtra("position", position);
                intentFiltered.putExtra("lat", locationsArray.get(position).getLatitude());
                intentFiltered.putExtra("lng", locationsArray.get(position).getLongitude());
                intentFiltered.putExtra("photos", photos.get(position));
                startActivity(intentFiltered);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtered_places_to_visit, menu);
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
