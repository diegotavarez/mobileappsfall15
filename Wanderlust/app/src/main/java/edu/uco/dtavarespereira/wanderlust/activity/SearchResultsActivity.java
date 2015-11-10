package edu.uco.dtavarespereira.wanderlust.activity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.JSonData;
import edu.uco.dtavarespereira.wanderlust.R;

public class SearchResultsActivity extends Activity {
    private TextView txtQuery;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // get the action bar
        ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
            txtQuery.setText("Search Query: " + query);
            int error = 0;

            for(int i=0; i<query.length();i++){
                if (!(query.charAt(i) >= 'A' && query.charAt(i)<= 'Z' || query.charAt(i)>='a' && query.charAt(i)<='z' || query.charAt(i)==' '))
                    error++;
            }

            if(error==0) {
                new HttpGetTask().execute(query);
            }
            else {
                Toast.makeText(getApplicationContext(), "ERROR: Add a valid city name!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class HttpGetTask extends AsyncTask<String, Void, ArrayList<String>> {

        private static final String TAG = "HttpGetTask";
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            InputStream in = null;
            HttpURLConnection httpUrlConnection = null;
            ArrayList<String> resultArray = null;

            try {
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("q",params[0] + ",us") // city
                        .appendQueryParameter("mode", "json") // json format as result
                        .appendQueryParameter("units", "metric") // metric unit
                        .appendQueryParameter("APPID", "d5ec4c21045bdf4fbe86c6fd452fd299")
                        .build();

                URL url = new URL(builtUri.toString());
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());
                String data = readStream(in);
                resultArray = JSonData.getData(data);

            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (null != httpUrlConnection) {
                    httpUrlConnection.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            return resultArray;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result == null || result.size() == 0) {
                Toast.makeText(SearchResultsActivity.this,
                        "Invalid weather data. Possibly wrong city",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //temp.setText("Temperature: " + result.get(2) + (char) 0x00B0);
            //mapButton.setText("Lat: " + result.get(0) + " - " + "Long: " + result.get(1));
            //desc.setText("Weather: " + result.get(4));
            //speed.setText("Wind Speed: " + result.get(3));

            //latitude = result.get(0);
            //longitude = result.get(1);
            //temperature = result.get(2);
            Toast.makeText(getApplicationContext(),result.get(0), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchResultsActivity.this, CityDetailActivity.class);
            intent.putExtra("CITY_NAME", query);
            intent.putExtra("lat", Double.valueOf(result.get(0)));
            intent.putExtra("lng", Double.valueOf(result.get(1)));
            startActivity(intent);

        }


        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }
    }
}
