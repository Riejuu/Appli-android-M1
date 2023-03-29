package com.example.taskmanagement;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentCalendrier extends Fragment {

    FonctionsDatabase fdb = new FonctionsDatabase();

    public FragmentCalendrier() {

    }

    //la vue se crée apres le on create, donc ca crée des null pointer exception si on met le code du calendrier dans onCreate et non dans onViewCreated
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        CalendarView calendarView = getActivity().findViewById(R.id.calendarView);

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.evenements_cochable, parentLayout, false);

        TextView tv = view.findViewById(R.id.evenementNom);
        tv.setText(eve.nom);

        //change de couleur de fond pour avoir celui du type
        Drawable backgroundDrawableTV = tv.getBackground();
        backgroundDrawableTV.setColorFilter(Color.parseColor(fdb.getColorOfOneType(getActivity(),eve.type)), PorterDuff.Mode.SRC_ATOP);


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

        //meme couleur de fond
        Drawable backgroundDrawableIV = iv.getBackground();
        backgroundDrawableIV.setColorFilter(Color.parseColor(fdb.getColorOfOneType(getActivity(),eve.type)), PorterDuff.Mode.SRC_ATOP);


        parentLayout.addView(view);

    }



}


