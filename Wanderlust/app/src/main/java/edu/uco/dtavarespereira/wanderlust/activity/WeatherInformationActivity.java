package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.uco.dtavarespereira.wanderlust.R;

public class WeatherInformationActivity extends Activity {

    TextView city, description, temperature, humidity, tempMin, tempMax, windSpeed;

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

        Intent intent = getIntent();
        city.setText(intent.getStringExtra("city"));
        description.setText(intent.getStringExtra("descrip"));
        temperature.setText(intent.getStringExtra("temp"));
        humidity.setText(intent.getStringExtra("humidity"));
        tempMin.setText(intent.getStringExtra("tempMin"));
        tempMax.setText(intent.getStringExtra("tempMax"));
        windSpeed.setText(intent.getStringExtra("wSpeed"));

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
}
