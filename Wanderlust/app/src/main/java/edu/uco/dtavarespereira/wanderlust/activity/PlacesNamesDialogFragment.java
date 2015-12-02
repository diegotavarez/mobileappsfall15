package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uco.dtavarespereira.wanderlust.R;

public class PlacesNamesDialogFragment extends DialogFragment {

    TextView txtName, txtAdress, txtPhone, txtWebsite;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_places_names_dialog, container, false);

        txtName = (TextView) view.findViewById(R.id.nameView);
        txtAdress = (TextView) view.findViewById(R.id.address);
        txtPhone = (TextView) view.findViewById(R.id.phone);
        txtWebsite = (TextView) view.findViewById(R.id.website);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle mArgs = getArguments();
        String name = mArgs.getString("name");
        String information = mArgs.getString("information");

        String[] name_lat_lon = information.split("/");

        txtName.setText(name);
        if(name_lat_lon.length == 3) {
            txtAdress.setText(name_lat_lon[0]);
            txtPhone.setText(name_lat_lon[1]);
            txtWebsite.setText(name_lat_lon[2]);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
