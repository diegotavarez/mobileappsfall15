package edu.uco.dtavarespereira.wanderlust;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.List;

public class FavoriteCitiesAdapter extends BaseAdapter {
    private List<String> cities;
    private Context context;
    private View parent;
    public String cityName;

    public FavoriteCitiesAdapter(Context context, List<String> cities, View parent){
        this.context = context;
        this.cities = cities;
        this.parent = parent;
    }

    @Override
    public int getCount(){
        return cities.size();
    }

    public Object getItem(int position){
        return cities.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View converView, final ViewGroup parent)
    {
        View view = converView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.favorites_cities_item, parent, false);

        final TextView tvCityName = (TextView) view.findViewById(R.id.tv_city_name_item);
        tvCityName.setText(cities.get(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = tvCityName.getText().toString();
                int error = 0;

                for(int i=0; i<cityName.length();i++){
                    if (!(cityName.charAt(i) >= 'A' && cityName.charAt(i)<= 'Z' || cityName.charAt(i)>='a' && cityName.charAt(i)<='z' || cityName.charAt(i)==' '))
                        error++;
                }

                if(error==0) {
                    new HttpGetTask().execute(cityName);

                    Intent intent = new Intent(parent.getContext(), CityDetailActivity.class);
                    intent.putExtra("CITY_NAME", cityName);
                    parent.getContext().startActivity(intent);
                }
                else {
                    Toast.makeText(parent.getContext(), "ERROR: Add a valid city name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

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
                Toast.makeText(parent.getContext(),
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
            Toast.makeText(parent.getContext(),result.get(0), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(parent.getContext(), CityDetailActivity.class);
            intent.putExtra("CITY_NAME", cityName);
            intent.putExtra("lat", Double.valueOf(result.get(0)));
            intent.putExtra("lng", Double.valueOf(result.get(1)));
            parent.getContext().startActivity(intent);

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
