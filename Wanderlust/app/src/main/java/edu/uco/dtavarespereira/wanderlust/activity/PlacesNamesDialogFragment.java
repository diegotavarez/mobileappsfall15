package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.R;

public class PlacesNamesDialogFragment extends DialogFragment {

    private ArrayList<String> arrayPlaces = new ArrayList<String>();
    ListView lstPlaces;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_places_names_dialog, container, false);

        lstPlaces = (ListView) view.findViewById(R.id.listPlacesDialogFragment);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle mArgs = getArguments();
        String name = mArgs.getString("name");

        arrayPlaces.add(name);

        lstPlaces.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.places_text, R.id.txtList, arrayPlaces));

        lstPlaces.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lstPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arrayPlaces.clear();
    }
}
