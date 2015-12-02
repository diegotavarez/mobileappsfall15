package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.adapter.FavoriteCitiesAdapter;
import edu.uco.dtavarespereira.wanderlust.adapter.FavoritePlacesAdapter;
import edu.uco.dtavarespereira.wanderlust.entity.Place;
import edu.uco.dtavarespereira.wanderlust.persistence.DataBaseStorage;

public class FavoritePlacesActivity extends Activity {
    ListView listPlaces;

    private static DataBaseStorage dbHelper;

    public static DataBaseStorage getDBHelper() {
        return dbHelper;
    }

    public static void setDBHelper(final DataBaseStorage newDBHelper) {
        FavoritePlacesActivity.dbHelper = newDBHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        try {
            setDBHelper(new DataBaseStorage(getApplicationContext()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        View rootView = findViewById(android.R.id.content);

        List<Place> places = FavoritePlacesActivity.getDBHelper().getPlaces();

        listPlaces = (ListView) findViewById(R.id.list_cities);
        FavoritePlacesAdapter adapter = new FavoritePlacesAdapter(getBaseContext(), places, rootView);
        listPlaces.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite_places, menu);
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
