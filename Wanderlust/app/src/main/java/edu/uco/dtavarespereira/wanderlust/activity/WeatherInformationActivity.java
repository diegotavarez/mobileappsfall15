package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.uco.dtavarespereira.wanderlust.R;

public class WeatherInformationActivity extends Activity {

    TextView city, description, temperature, humidity, tempMin, tempMax, windSpeed;
    private static ImageView imageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_information);

        city = (TextView)findViewById(R.id.textViewCity);
        description = (TextView)findViewById(R.id.textViewDescription);
        temperature = (TextView)findViewById(R.id.textViewTemp);
        humidity = (TextView)findViewById(R.id.textViewHumidity);
        tempMin = (TextView)findViewById(R.id.textViewMin);
        tempMax = (TextView)findViewById(R.id.textViewMax);
        windSpeed = (TextView)findViewById(R.id.textViewWinSpeed);
	imageDescription = (ImageView) findViewById(R.id.imageDescription);

        Intent intent = getIntent();
        city.setText(intent.getStringExtra("city"));
        description.setText(intent.getStringExtra("descrip"));
        temperature.setText(intent.getStringExtra("temp") + (char) 0x00B0);
        humidity.setText(intent.getStringExtra("humidity") + "%");
        tempMin.setText(intent.getStringExtra("tempMin"));
        tempMax.setText(intent.getStringExtra("tempMax"));
        windSpeed.setText(intent.getStringExtra("wSpeed") + "mph");

        defineImage(Integer.parseInt(intent.getStringExtra("id")), imageDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_information, menu);
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
