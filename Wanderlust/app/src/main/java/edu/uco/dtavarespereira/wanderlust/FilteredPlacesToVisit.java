package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FilteredPlacesToVisit extends Activity {
    ListView places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_places_to_visit);

        places = (ListView) findViewById(R.id.places);
      Intent intent = getIntent();
        int j = intent.getIntExtra("size", 0);
        ArrayList<ArrayList<String>> placesNames = new ArrayList<>();

        for(int i = 0; i < j; i++){
            placesNames.add(i,intent.getStringArrayListExtra("data " + i));
        }
        
      ArrayList<String> names = new ArrayList<>();
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


     /* ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this, R.array.barsPlacesList, android.R.layout.simple_list_item_1);
        places.setAdapter(adapter);*/

       /* places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), " it's working", Toast.LENGTH_SHORT).show();
                Intent intentFiltered = new Intent(FilteredPlacesToVisit.this, PlaceInformation.class);
                startActivity(intentFiltered);
            }
        });*/



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
