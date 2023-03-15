package com.example.taskmanagement;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;


public class AccueilFragment extends Fragment {


    FonctionsDatabase fdb = new FonctionsDatabase();

    public AccueilFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //on utilise onCreateView et non onViewCreated, car si on est deja sur l accueil et on rappuie sur le bouton accueil
    //le graphique ne se re affichera pas
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_accueil, container, false);

        //exemple de comment utiliser les graphiques



        if(fdb.getAllTypes(getActivity()).size() != 0 ) {


            for(Types t : fdb.getAllTypes(getActivity())) {

                PieChart chart = rootView.findViewById(R.id.pieGraph);

                int g2 = 35;
                int g3 = 50;

                chart.addPieSlice(new PieModel("a", g2, Color.parseColor("#FF0000")));
                chart.addPieSlice(new PieModel("tous", g3, Color.parseColor("#0000FF")));

                chart.startAnimation();
            }
        }

        return rootView;

    }
}