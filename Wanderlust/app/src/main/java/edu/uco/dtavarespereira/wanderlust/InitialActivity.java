package edu.uco.dtavarespereira.wanderlust;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

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

public class InitialActivity extends Activity {

    ImageButton btSearch;
    EditText etCityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        btSearch = (ImageButton) findViewById(R.id.bt_search);
        etCityName = (EditText)findViewById(R.id.editText_city);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCityName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please type a city", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InitialActivity.this, CityDetailActivity.class);
                    intent.putExtra("CITY_NAME", etCityName.getText().toString());
                    startActivity(intent);
                }
            }
        });

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
