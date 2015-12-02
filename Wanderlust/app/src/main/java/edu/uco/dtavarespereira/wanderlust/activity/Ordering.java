package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.uco.dtavarespereira.wanderlust.PlacesDetailsSearch;
import edu.uco.dtavarespereira.wanderlust.R;


public class Ordering extends DialogFragment {
    String[] orderByItems;
    ListView orderList;
    boolean click;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderByItems = getResources().getStringArray(R.array.orderBy);

        click = false;
        View view = inflater.inflate(R.layout.fragment_ordering, container, false);
        orderList = (ListView) view.findViewById(R.id.orderList);


        orderList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.fragment_ordering_textview, orderByItems));
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    PlacesDetailsSearch.orderByHigherRating();
                } else if (position == 1) {
                    PlacesDetailsSearch.orderByLowerRating();
                } else if (position == 2) {
                    PlacesDetailsSearch.orderByName();
                }
                FilteredPlacesToVisit.ordered = true;
                click = true;
                onClick();

            }
        });


        return view;

    }

    public static interface OnCompleteListener {
        public abstract void onComplete(String time);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public void onClick(){
        this.mListener.onComplete("");
    }
}
