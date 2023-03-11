package com.example.taskmanagement;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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


        CalendarView calendarView = getActivity().findViewById(R.id.calendarView);
        FonctionsDatabase fdb = new FonctionsDatabase();


        fdb.addEvenement(getActivity(), "bonjour", "menage", 2023, 3, 3, 5, 5);
        //fdb.deleteEvenement(getActivity(), 5);
        fdb.showAllEvenement(getActivity());

        /*
        fdb.addTypes(getActivity(), "menage");
        fdb.deleteTypes(getActivity(), "menage");
        fdb.showAllTypes(getActivity());
        */


        //quand on selectionne une case du calendrier recupère ou on est et
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {



                //les mois commencent a 0
                for(Evenement eve : fdb.showDaysEvent(getActivity(), year, month+1, dayOfMonth))
                    afficherEvenementCalendrier(eve);


            }
        });








    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendrier, container, false);
    }






    //ecrit l'evenement sous le calendrier dans un TextView selon les resultats données par la fonction showDaysEvent qui renvoie tous
    //les evenements du jour

    public void afficherEvenementCalendrier(Evenement eve){

        TextView textView = new TextView(getContext());
        textView.setText(eve.nom);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setPadding(16, 16, 16, 16);
        LinearLayout linearLayout = getView().findViewById(R.id.editTextLayout);
        linearLayout.addView(textView);


        }

}


