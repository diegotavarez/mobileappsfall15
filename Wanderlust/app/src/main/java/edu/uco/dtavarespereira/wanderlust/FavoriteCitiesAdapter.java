package edu.uco.dtavarespereira.wanderlust;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FavoriteCitiesAdapter extends BaseAdapter {
    private List<String> cities;
    private Context context;

    public FavoriteCitiesAdapter(Context context, List<String> cities){
        this.context = context;
        this.cities = cities;
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

    public View getView(int position, View converView, ViewGroup parent)
    {
        View view = converView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.favorites_cities_item, parent, false);

        TextView tvCityName = (TextView) view.findViewById(R.id.tv_city_name_item);
        tvCityName.setText(cities.get(position));
        return view;
    }



}
