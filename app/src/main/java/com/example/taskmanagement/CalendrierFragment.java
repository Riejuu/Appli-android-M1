package com.example.taskmanagement;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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


    FonctionsDatabase fdb = new FonctionsDatabase();

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



        //fdb.addEvenement(getActivity(), "test", "Travail", 2023, 3, 3, 5, 5, 0);
        //fdb.deleteEvenement(getActivity(), 5);
        //fdb.showAllEvenement(getActivity());

        /*
        fdb.addTypes(getActivity(), "menage");
        fdb.deleteTypes(getActivity(), "menage");
        fdb.showAllTypes(getActivity());
        */


        //quand on selectionne une case du calendrier recupère ou on est et
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {


                LinearLayout linearLayout = getView().findViewById(R.id.linearLayoutCalendrier);
                linearLayout.removeAllViews();

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






    /*ecrit l'evenement sous le calendrier dans un TextView selon les resultats données par la fonction showDaysEvent qui renvoie tous
    les evenements du jour dans des rectangles arrondis
    */
    public void afficherEvenementCalendrier(Evenement eve){

        LinearLayout parentLayout = getActivity().findViewById(R.id.linearLayoutCalendrier);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.calendrier_evenements, parentLayout, false);

        TextView tv = view.findViewById(R.id.evenementNom);
        tv.setText("hey");

        //je recupere l image et lui affecte un un on click effect mais aussi l image associé a si on a validé ou non
        ImageView iv = view.findViewById(R.id.evenementValide); //modifier cette ligne
        iv.setImageResource( (eve.valide)? R.drawable.tache_faite : R.drawable.tache_pas_faite );
        iv.setId(eve.id);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fdb.alterValideEvenement(getActivity(), eve.id,(eve.valide)?  0:1);                             //modifie la valeur de valide dans la db
                iv.setImageResource( (eve.valide)? R.drawable.tache_pas_faite : R.drawable.tache_faite );       //modifie l'image a chaque clique
                eve.valide = (eve.valide)? false :  true;                                                       //modifie la valeur de valide de l'evenement


            }
        });

        parentLayout.addView(view);

    }



}


