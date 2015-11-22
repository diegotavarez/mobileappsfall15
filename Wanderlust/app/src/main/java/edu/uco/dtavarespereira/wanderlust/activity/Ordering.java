package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.uco.dtavarespereira.wanderlust.R;


public class Ordering extends DialogFragment {
    String[] orderByItems;
    ListView orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderByItems = getResources().getStringArray(R.array.orderBy);

        View view = inflater.inflate(R.layout.fragment_ordering, container, false);
        orderList = (ListView) view.findViewById(R.id.orderList);

        orderList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.fragment_ordering_textview, orderByItems));
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        return view;

    }
}
