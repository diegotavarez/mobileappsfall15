package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.ActionBar;
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
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.PlacesDetailsSearch;
import edu.uco.dtavarespereira.wanderlust.PlacesSearch;
import edu.uco.dtavarespereira.wanderlust.R;

public class CommercialPlacesActivity extends Activity {

    private final static String TAG = "GoogleSearchAsyncTask";
    ListView lstCommercialPlaces;
    ArrayAdapter<CharSequence> adapter, adp;
    ArrayList<Location> locationArray;
    Location location = new Location("");
    String BASE_URL = "https://maps.googleapis.com/maps/api/place/radarsearch/json?";
    String BASE_URL_DETAILS_SEARCH = "https://maps.googleapis.com/maps/api/place/details/json?";
    String category;
    ArrayList<ArrayList<String>> placesNames = new ArrayList<>();
    ArrayList<String> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_places);

        lstCommercialPlaces = (ListView) findViewById(R.id.lstCommercialPlaces);
        adapter = ArrayAdapter.createFromResource(this, R.array.commercialPlacesList, android.R.layout.simple_list_item_1);
        adp = ArrayAdapter.createFromResource(this,R.array.commercialPlacesListNoFilterList, android.R.layout.simple_list_item_1);
        lstCommercialPlaces.setAdapter(adp);


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String city = intent.getStringExtra("cityName");
        setTitle(city);
        city.trim();
        final Location location = new Location(city);
        location.setLatitude(intent.getDoubleExtra("locationLatitude", 0.0));
        location.setLongitude(intent.getDoubleExtra("locationLongitude", 0.0));

        lstCommercialPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), " it's working", Toast.LENGTH_SHORT).show();

                category = lstCommercialPlaces.getItemAtPosition(position).toString();
                new GoogleSearchASyncTask().execute(new String[]{String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), adapter.getItem(position).toString()});
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

        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
        }

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    class GoogleSearchASyncTask extends AsyncTask<String, String, ArrayList> {
        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList<String> s = new ArrayList<>();
            s.add(params[0]);
            ArrayList<String> resultArray = null;
            locationArray = new ArrayList<>();
            photos = new ArrayList<>();
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                InputStream in = null;
                HttpURLConnection httpUrlConnection = null;
                try{
                    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter("location", params[0] + "," + params[1])
                            .appendQueryParameter("radius", "5000")
                            .appendQueryParameter("types", params[2])
                            .appendQueryParameter("key", "AIzaSyBRfUWJUz5x9TnFaIUbqjsrKC_q_mTBIQo")
                            .build();

                    URL url = new URL(builtUri.toString());
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    in = new BufferedInputStream(
                            httpUrlConnection.getInputStream());
                    String data = readStream(in);
                    PlacesSearch.getIds().removeAll(PlacesSearch.getIds());
                    resultArray = PlacesSearch.getData(data, 0);
                    int k = PlacesSearch.getLenght();
                    for(int i = 1; i < k; i++){
                        resultArray = PlacesSearch.getData(data, i);
                    } //TODO array needs to be array
                    httpUrlConnection.disconnect();

                    in = null;
                    httpUrlConnection = null;
                    int i = 0;
                    PlacesDetailsSearch.placeArray.removeAll(PlacesDetailsSearch.placeArray);
                    for(String places : resultArray) {
                        builtUri = Uri.parse(BASE_URL_DETAILS_SEARCH).buildUpon()
                                .appendQueryParameter("placeid", places)
                                .appendQueryParameter("key", "AIzaSyBRfUWJUz5x9TnFaIUbqjsrKC_q_mTBIQo")
                                .build();
                        URL url1 = new URL(builtUri.toString());
                        httpUrlConnection = (HttpURLConnection) url1.openConnection();
                        in = new BufferedInputStream(
                                httpUrlConnection.getInputStream());
                        String data1 = readStream(in);


                        ArrayList<String> placesData = PlacesDetailsSearch.getData(data1);
                        System.out.println("PLACE DATA IS" + placesData);
                        placesNames.add(i, placesData);
                        locationArray.add(PlacesDetailsSearch.getLocation());
                        photos.add(PlacesDetailsSearch.getPhoto());
                        i++;
                    }
                    resultArray.removeAll(resultArray);

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
                Toast.makeText(getApplicationContext(), "No internet Connection",
                        Toast.LENGTH_LONG).show();
            }
            return resultArray;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            // Toast.makeText(getApplicationContext(), placesNames.toString(), Toast.LENGTH_SHORT).show();

            if(!placesNames.isEmpty()) {
                Intent intentFiltered = new Intent(CommercialPlacesActivity.this, FilteredPlacesToVisit.class);
                intentFiltered.putExtra("size", placesNames.size());
                for (int i = 0; i < placesNames.size(); i++) {
                    intentFiltered.putExtra("data " + i, placesNames.get(i));
                    intentFiltered.putExtra("location lat " + i, locationArray.get(i).getLatitude());
                    intentFiltered.putExtra("location lng " + i, locationArray.get(i).getLongitude());
                }
                intentFiltered.putExtra("photos", photos);
                intentFiltered.putExtra("category", category);
                placesNames.removeAll(placesNames);
                locationArray.removeAll(locationArray);
                startActivity(intentFiltered);
            } else {
                Toast.makeText(getApplicationContext(), "Sorry! Google API is limited. Please try again later.", Toast.LENGTH_SHORT).show();
            }
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
