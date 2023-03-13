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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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


        fdb.addEvenement(getActivity(), "bonjourhbnbhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", "menage", 2023, 3, 3, 5, 5, 0);
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






    /*ecrit l'evenement sous le calendrier dans un TextView selon les resultats données par la fonction showDaysEvent qui renvoie tous
    les evenements du jour dans des rectangles arrondis
    */
    public void afficherEvenementCalendrier(Evenement eve){

        // cree un textView
        TextView textView = new TextView(getContext());
        textView.setText(eve.type + " : " + eve.nom);



        // je fais un rectangle arrondis
        float radius = getResources().getDisplayMetrics().density * 16;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });   //arrondi de 16dp dans chaque coin
        shape.setStroke(1, Color.BLACK);    //bordure de 1 pixel

        //adapter a la couleur du type
        shape.setColor(Color.GREEN);    //TODO CHANGER CETTE LIGNE PAR LA COULEUR DU TYPE QUAND CELA SERA FAIT

        //j applique ces changements de forme et de couleur au textview
        textView.setBackground(shape);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        //j augmente la taille du cadre aussi en dp
        int paddingHautBas = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());//espace haut et bas
        int paddingDroite = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());//espace droite
        textView.setPadding(textView.getPaddingLeft()+10, paddingHautBas, paddingDroite, paddingHautBas);
        LinearLayout linearLayout = getView().findViewById(R.id.editTextLinearLayoutCalendrier);


        //une ligne max, sinon on ecrit ...

        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(true);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Définir la taille du texte en dp



        //rajout de l image validé, non validé
/*
        ImageView imageView = getActivity().findViewById(R.id.tache_pas_faite);
        imageView.setImageResource(img);
*/









        //j'espace les textView
        LinearLayout.LayoutParams espacement = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        espacement.setMargins(0, 0, 0, (int) getResources().getDisplayMetrics().density * 25);
        textView.setLayoutParams(espacement);

        linearLayout.addView(textView);
    }

}


