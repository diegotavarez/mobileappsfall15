package edu.uco.dtavarespereira.wanderlust.activity;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uco.dtavarespereira.wanderlust.R;

public class DepartmentNamesDialogFragment extends DialogFragment {

    private ArrayList<String> arrayDepartments = new ArrayList<String>();
    private ArrayList<String> arrayWebPages = new ArrayList<String>();
    ListView listDepartments;
    private int intNotificationCount;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_department_names_dialog, container, false);

        listDepartments = (ListView) view.findViewById(R.id.listDepartmentsDialogFragment);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle mArgs = getArguments();
        String name = mArgs.getString("name");
       // String address = mArgs.getString("address");
      //  String phone = mArgs.getString("phone");
      //  String website = mArgs.getString("website");

        arrayDepartments.add(name);
      //  arrayDepartments.add(address);
      //  arrayDepartments.add(phone);

        arrayWebPages.add(name);
       // arrayWebPages.add(website);
      //  arrayWebPages.add(website);

        listDepartments.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.contact_and_department_name, R.id.txtList, arrayDepartments));

        listDepartments.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentWeb = new Intent(Intent.ACTION_VIEW);
                intentWeb.setData(Uri.parse(arrayWebPages.get(position).toString()));


                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getActivity(),
                                0,
                                intentWeb,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                Notification.Builder notificationBuilder = new Notification.Builder(getActivity())
                        .setSmallIcon(android.R.drawable.ic_menu_myplaces)
                        .setAutoCancel(true)
                        .setContentTitle("(" + ++intNotificationCount + ") " + arrayDepartments.get(position).toString() + " Button Pressed!")
                        .setContentText("Click to go to " + arrayDepartments.get(position).toString()
                                + " webpage!")
                        .setContentIntent(resultPendingIntent);

                NotificationManager notificationManager = (NotificationManager)
                        getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arrayDepartments.clear();
        arrayWebPages.clear();
    }
}
