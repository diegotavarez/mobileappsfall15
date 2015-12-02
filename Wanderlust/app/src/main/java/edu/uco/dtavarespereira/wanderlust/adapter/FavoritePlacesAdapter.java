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

import edu.uco.dtavarespereira.wanderlust.JSonData;
import edu.uco.dtavarespereira.wanderlust.R;
import edu.uco.dtavarespereira.wanderlust.activity.CityDetailActivity;
import edu.uco.dtavarespereira.wanderlust.activity.FavoritePlaceInformationActivity;
import edu.uco.dtavarespereira.wanderlust.entity.Place;

public class FavoritePlacesAdapter extends BaseAdapter {
    private List<Place> places;
    private Context context;
    private View parent;
    public String cityName;
    private Place place;

    public FavoritePlacesAdapter(Context context, List<Place> places, View parent){
        this.context = context;
        this.places = places;
        this.parent = parent;
    }

    @Override
    public int getCount(){
        return places.size();
    }

    public Object getItem(int position){
        return places.get(position);
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

        place = places.get(position);

        tvCityName.setText(capitalize(place.getName()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFiltered = new Intent(parent.getContext(), FavoritePlaceInformationActivity.class);
                intentFiltered.putExtra("name", place.getName());
                intentFiltered.putExtra("address", place.getAddress());
                intentFiltered.putExtra("website", place.getWebsite());
                intentFiltered.putExtra("phone", place.getPhoneNumber());
                intentFiltered.putExtra("rating", place.getRating());
                intentFiltered.putExtra("position", 0);
                intentFiltered.putExtra("lat", 0);
                intentFiltered.putExtra("lng", 0);
                intentFiltered.putExtra("photos", 0);
                intentFiltered.putExtra("category", place.getCategory());
                parent.getContext().startActivity(intentFiltered);
            }
        });

        return view;

    }


    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
