package edu.uco.dtavarespereira.wanderlust.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

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

import edu.uco.dtavarespereira.wanderlust.JSonData;
import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.activity.CityDetailActivity;
import edu.uco.dtavarespereira.wanderlust.entity.Forecast;

public class WeeklyForecastAdapter extends BaseAdapter {
    private List<Forecast> weeklyForecast;
    private Context context;
    private View parent;
    public String cityName;

    public WeeklyForecastAdapter(Context context, List<Forecast> weeklyForecast, View parent){
        this.context = context;
        this.weeklyForecast = weeklyForecast;
        this.parent = parent;
    }

    @Override
    public int getCount(){
        return weeklyForecast.size();
    }

    public Object getItem(int position){
        return weeklyForecast.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View converView, final ViewGroup parent)
    {
        Forecast forecast = weeklyForecast.get(position);

        View view = converView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.weekly_forecast_item, parent, false);

        final TextView tvDayOfTheWeek = (TextView) view.findViewById(R.id.tv_day);
        tvDayOfTheWeek.setText(forecast.getDayOfTheWeek());

        final TextView tvMinimum = (TextView) view.findViewById(R.id.tv_minimum);
        tvMinimum.setText(forecast.getMinimumTemperature());

        final TextView tvMaximum = (TextView) view.findViewById(R.id.tv_maximum);
        tvMaximum.setText(forecast.getMaximumTemperature());

        final ImageView imWeather = (ImageView) view.findViewById(R.id.weather_icon);
        defineImage(forecast.getId(), imWeather);

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
            Intent intent = new Intent(parent.getContext(), CityDetailActivity.class);
            intent.putExtra("CITY_NAME", cityName);
            intent.putExtra("lat", Double.valueOf(result.get(0)));
            intent.putExtra("lng", Double.valueOf(result.get(1)));
            intent.putExtra("temperature", result.get(2));
            intent.putExtra("humidity", result.get(3));
            intent.putExtra("temp_min", result.get(4));
            intent.putExtra("temp_max", result.get(5));
            intent.putExtra("windSpeed", result.get(6));
            intent.putExtra("id", result.get(7));
            intent.putExtra("description", result.get(8));
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

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static void defineImage(int id, ImageView imageView) {

        if (id >= 200 && id <= 232) {
            imageView.setImageResource(R.mipmap.thunderstorm);
        } else if (id >= 300 && id <= 321) {
            imageView.setImageResource(R.mipmap.shower_rain);
        } else if (id >= 500 && id <= 531) {
            imageView.setImageResource(R.mipmap.rain);
        } else if (id >= 600 && id <= 622) {
            imageView.setImageResource(R.mipmap.snow);
        } else if (id >= 701 && id <= 781) {
            imageView.setImageResource(R.mipmap.mist);
        } else if (id == 800) {
            imageView.setImageResource(R.mipmap.clear_sky);
        } else if (id == 801) {
            imageView.setImageResource(R.mipmap.few_clouds);
        } else if (id == 802) {
            imageView.setImageResource(R.mipmap.scattered_clouds);
        } else if (id == 803) {
            imageView.setImageResource(R.mipmap.broken_clouds);
        }else{
            imageView.setImageResource(R.mipmap.scattered_clouds);
        }

    }
}
