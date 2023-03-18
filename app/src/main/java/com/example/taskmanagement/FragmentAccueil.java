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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;


public class FragmentAccueil extends Fragment {


    FonctionsDatabase fdb = new FonctionsDatabase();

    public FragmentAccueil() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //on veut mettre des layout vertical a chaque fois que je depasse deux types par ligne, donc on met un linearlayout dans un xml different et le pie pareil
        //l idée c est d include le linear une fois tous les deux pie d include

        //donc on get le layout de l acceuil
        LinearLayout linearLayoutVertical = getActivity().findViewById(R.id.linearLayoutAccueilVertical);




        //on lui donne un layout en ligne, initialiser dans le if d apres mais besoin d etre declaré plus tot pour etre accessible par vuePieChart a la creation
        LinearLayout horizontalLayout = null;





        int i = 1;
        if(fdb.getAllTypes(getActivity()).size() != 0 ) {


            for(Types t : fdb.getAllTypes(getActivity())) {

                // si on a deux piechart, on crée une nouvelle ligne
                if(i % 2 == 1) {
                    horizontalLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.accueil_linearlayout_horizontal,linearLayoutVertical,false);
                    linearLayoutVertical.addView(horizontalLayout);
                }

                //je veux pas que la premiere ligne ait un padding de 70dp mais les autres si, c est pour ca que je ne l ai pas mis dans le xml
                if(i >2 ) {
                    int paddingTop = (int) (70 * getResources().getDisplayMetrics().density); //50dp
                    horizontalLayout.setPadding(0, paddingTop, 0, 0);
                }

                //et dans ce layout 2 fois on lui donne des pie chart
                View vuePieChart = LayoutInflater.from(getActivity()).inflate(R.layout.accueil_graphique_circulaire,horizontalLayout, false);

                PieChart chart = vuePieChart.findViewById(R.id.pieGraph);
                int g2 = 35;
                int g3 = 65;

                
                //rempli de la couleur du type
                chart.addPieSlice(new PieModel("a", g2, Color.parseColor(fdb.getColorOfOneType(getActivity(),t.type))));
                chart.addPieSlice(new PieModel("tous", g3, Color.parseColor("#DDDDDD")));
                chart.startAnimation();

                TextView titreTV = vuePieChart.findViewById(R.id.accueilTitrePieChart);
                titreTV.setText(t.type);            // TODO rajouter le nombre de validé


                horizontalLayout.addView(vuePieChart);
                i++;
            }



        }






    }

    //on utilise onCreateView et non onViewCreated, car si on est deja sur l accueil et on rappuie sur le bouton accueil
    //le graphique ne se re affichera pas
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_accueil, container, false);

        //LinearLayout linearLayoutVertical = getActivity().findViewById(R.id.linearLayoutAccueilVertical);
        //LinearLayout linearLayoutHorizontal = getActivity().findViewById(R.id.linearLayoutAccueilHorizontale);
        //exemple de comment utiliser les graphiques

        //View linearPieChart =  LayoutInflater.from(getActivity()).inflate(R.layout.accueil_graphique_circulaire, linearLayoutHorizontal, false);

       /// linearLayoutHorizontal.addView(linearPieChart);


        /*

        if(fdb.getAllTypes(getActivity()).size() != 0 ) {

            PieChart chart = rootView.findViewById(R.id.pieGraph);


            for(Types t : fdb.getAllTypes(getActivity())) {

               // PieChart chart = rootView.findViewById(R.id.pieGraph);

                int g2 = 35;
                int g3 = 50;

                chart.addPieSlice(new PieModel("a", g2, Color.parseColor("#FF0000")));
                chart.addPieSlice(new PieModel("tous", g3, Color.parseColor("#0000FF")));

                chart.startAnimation();
            }
        }
*/
        return rootView;

    }
}