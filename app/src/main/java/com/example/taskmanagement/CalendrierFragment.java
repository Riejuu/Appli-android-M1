package com.example.taskmanagement;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendrierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendrierFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendrierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendrierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendrierFragment newInstance(String param1, String param2) {
        CalendrierFragment fragment = new CalendrierFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //la vue se crée apres le on create, donc ca crée des null pointer exception si on met le code du calendrier dans onCreate et non dans onViewCreated
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CalendarView calendarView = (CalendarView) getActivity().findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Faites quelque chose lorsque l'utilisateur sélectionne une nouvelle date dans le calendrier
                System.out.println("hey salut");
            }
        });


        // Récupération du ContentResolver
        ContentResolver cr = getActivity().getContentResolver();

        // Création d'un nouvel événement
        ContentValues values = new ContentValues();
        int date = 5;
        values.put(CalendarContract.Events.DTSTART, date);
        values.put(CalendarContract.Events.DTEND, date);
        values.put(CalendarContract.Events.TITLE, "Mon nouvel événement");
        values.put(CalendarContract.Events.DESCRIPTION, "Description de l'événement");
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        // Ajout de l'événement au calendrier
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // Affichage de la confirmation
        //Toast.makeText(getActivity(), "Evénement ajouté au calendrier", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendrier, container, false);
    }
}