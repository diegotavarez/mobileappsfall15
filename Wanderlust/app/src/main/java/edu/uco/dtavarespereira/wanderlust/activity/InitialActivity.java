package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
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
import java.util.List;
import java.util.Locale;

import edu.uco.dtavarespereira.wanderlust.JSonData;
import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.persistence.DataBaseStorage;
import edu.uco.dtavarespereira.wanderlust.util.FieldValidation;

public class InitialActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    ImageButton btSearch;
    EditText etCityName;
    String cityName;

    boolean location_button_clicked = false;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private static DataBaseStorage dbHelper;

    public static DataBaseStorage getDBHelper() {
        return dbHelper;
    }

    public static void setDBHelper(final DataBaseStorage newDBHelper) {
        InitialActivity.dbHelper = newDBHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        location_button_clicked = false;

        try {
            setDBHelper(new DataBaseStorage(getApplicationContext()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<String> favorites = getStringArrayPref(getApplicationContext(), "FAVORITES");
        if (favorites.size() > 0)
        {
            InitialActivity.this.finish();
            Intent intentFav = new Intent(InitialActivity.this, FavoritesActivity.class);
            intentFav.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentFav);
        }

        btSearch = (ImageButton) findViewById(R.id.bt_search);
        etCityName = (EditText)findViewById(R.id.editText_city);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    cityName = etCityName.getText().toString();
                    int error = 0;

                    for (int i = 0; i < cityName.length(); i++) {
                        if (!(cityName.charAt(i) >= 'A' && cityName.charAt(i) <= 'Z' || cityName.charAt(i) >= 'a' && cityName.charAt(i) <= 'z' || cityName.charAt(i) == ' '))
                            error++;
                    }

                    if (error == 0) {
                        new HttpGetTask().execute(cityName);
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Add a valid city name!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
                /*if (etCityName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please type a city", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InitialActivity.this, CityDetailActivity.class);
                    intent.putExtra("CITY_NAME", etCityName.getText().toString());
                    startActivity(intent);
                }
            }*/
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        location_button_clicked = false;
    }


    private boolean checkValidation() {
        boolean ret = true;
        final FieldValidation validation = new FieldValidation(InitialActivity.this);
        if (!validation.hasText(etCityName)) {
            ret = false;
        }

        return ret;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            Intent intentFavorites = new Intent(InitialActivity.this, FavoritePlacesActivity.class);
            startActivity(intentFavorites);
        }

        if (id == R.id.action_current_position){
            buildGoogleApiClient();
            mGoogleApiClient.connect();
            location_button_clicked = true;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            new HttpGetTask().execute(cityName);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class HttpGetTask extends AsyncTask<String, Void, ArrayList<String>> {

        private static final String TAG = "HttpGetTask";
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            InputStream in = null;
            HttpURLConnection httpUrlConnection = null;
            ArrayList<String> resultArray = null;

            try {
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("q", params[0] + ",us") // city
                        .appendQueryParameter("mode", "json") // json format as result
                        .appendQueryParameter("units", "metric") // metric unit
                        .appendQueryParameter("cnt", "7")
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
                Toast.makeText(InitialActivity.this,
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


            Intent intent = new Intent(InitialActivity.this, CityDetailActivity.class);
            /*intent.putExtra("CITY_NAME", etCityName.getText().toString());
            intent.putExtra("lat", Double.valueOf(result.get(0)));
            intent.putExtra("lng", Double.valueOf(result.get(1)));
            intent.putExtra("temperature", result.get(2));
            intent.putExtra("humidity", result.get(3));
            intent.putExtra("temp_min", result.get(4));
            intent.putExtra("temp_max", result.get(5));
            intent.putExtra("windSpeed", result.get(6));
	        intent.putExtra("id", result.get(7));
            intent.putExtra("description", result.get(8));
            startActivity(intent);*/

            //String city = result.remove(0);
            /*
            if(location_button_clicked == true)
            {
                ArrayList<String> result2 = new ArrayList<>();

                String[] name_lat_lon = result.get(0).split("/");

                result2.add(name_lat_lon[0] + "/" + mLastLocation.getLatitude() + "/" + mLastLocation.getLongitude());
                result2.add(result.get(1));
                intent.putStringArrayListExtra("listDays", result2);


            }
            else
            */
            intent.putStringArrayListExtra("listDays", result);

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
