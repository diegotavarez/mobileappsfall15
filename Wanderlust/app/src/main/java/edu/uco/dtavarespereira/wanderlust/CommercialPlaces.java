package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommercialPlaces extends Activity {

    private final static String TAG = "GoogleSearchAsyncTask";
    ListView lstCommercialPlaces;
    ArrayAdapter<CharSequence> adapter;
    Location location = new Location("");
    String BASE_URL = "https://maps.googleapis.com/maps/api/place/radarsearch/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_places);

        lstCommercialPlaces = (ListView) findViewById(R.id.lstCommercialPlaces);
        adapter = ArrayAdapter.createFromResource(this, R.array.commercialPlacesList, android.R.layout.simple_list_item_1);

        lstCommercialPlaces.setAdapter(adapter);


        Intent intent = getIntent();
        String city = intent.getStringExtra("cityName");
        final Location location = new Location(city);
        location.setLatitude(intent.getDoubleExtra("locationLatitude", 0.0));
        location.setLongitude(intent.getDoubleExtra("locationLongitude", 0.0));

        lstCommercialPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), " it's working", Toast.LENGTH_SHORT).show();
                new GoogleSearchASyncTask().execute(new String[]{String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), lstCommercialPlaces.getItemAtPosition(position).toString()});
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commercial_places, menu);
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

    class GoogleSearchASyncTask extends AsyncTask<String, String, ArrayList> {
        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList<String> s = new ArrayList<>();
            s.add(params[0]);
            ArrayList<String> resultArray = null;
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                InputStream in = null;
                HttpURLConnection httpUrlConnection = null;
                try{
                    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter("location", params[0] + "," + params[1])
                            .appendQueryParameter("radius", "500")
                            .appendQueryParameter("types", params[2])
                            .appendQueryParameter("key", "AIzaSyBRfUWJUz5x9TnFaIUbqjsrKC_q_mTBIQo")
                            .build();

                    URL url = new URL(builtUri.toString());
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    in = new BufferedInputStream(
                            httpUrlConnection.getInputStream());
                    String data = readStream(in);
                    resultArray = PlacesSearch.getData(data, 0);
                    location = PlacesSearch.getLocation();
                } catch (MalformedURLException exception){
                    Log.e(TAG, "MalFormedURLException");
                } catch (IOException exception){
                    Log.e(TAG, "IOException");
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    e.printStackTrace();
                } finally{
                    if (null != httpUrlConnection){
                        httpUrlConnection.disconnect();
                    }
                }
            } else {
                // display error
            }
            return resultArray;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            //TODO

            Intent intentMaps = new Intent(CommercialPlaces.this, MapsActivity.class);
            intentMaps.putExtra("lat", location.getLatitude());
            intentMaps.putExtra("lon", location.getLongitude());

            startActivity(intentMaps);

        }

    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
