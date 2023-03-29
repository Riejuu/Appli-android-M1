package com.example.taskmanagement;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentAccueil extends Fragment {


    FonctionsDatabase fdb = new FonctionsDatabase();

    public FragmentAccueil() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifConnexion();   //notif de création d'appli
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afficherAccueil();  //crée la page
    }





    public void afficherAccueil(){


        //on veut mettre des layout vertical a chaque fois que je depasse deux types par ligne, donc on met un linearlayout dans un xml different et le pie pareil
        //l idée c est d include le linear une fois tous les deux pie d include

        //donc on get le layout de l acceuil
        LinearLayout linearLayoutVertical = getActivity().findViewById(R.id.linearLayoutAccueilVertical);
        linearLayoutVertical.removeAllViews();



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
                int tachesTerminees = fdb.showTypeEventValideTrue(getActivity(),t.type).size();
                int nombreDeTache = fdb.showTypeEvent(getActivity(),t.type).size();


                //rempli de la couleur du type
                chart.addPieSlice(new PieModel("Tâches Terminees", tachesTerminees, Color.parseColor(fdb.getColorOfOneType(getActivity(),t.type))));
                chart.addPieSlice(new PieModel("Nombre de tâche", nombreDeTache-tachesTerminees, Color.parseColor("#DDDDDD")));
                chart.startAnimation();

                TextView titreTV = vuePieChart.findViewById(R.id.accueilTitrePieChart);
                titreTV.setText(t.type +  " " + tachesTerminees + "/" + nombreDeTache);


                //lui ajoute un onClick listener pour voir tous les evenements de ce type

                vuePieChart.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       afficherPopUpEvenements(getView(), t);

                                                   }
                                               }

                );

                horizontalLayout.addView(vuePieChart);
                i++;
            }

        }
    }




    public void afficherPopUpEvenements(View view, Types typeuh){

        PopupWindow popup = new PopupWindow(view, view.getLayoutParams().WRAP_CONTENT,  view.getLayoutParams().WRAP_CONTENT, true);

        View popupView = getLayoutInflater().inflate(R.layout.popup_liste_evenements, null);
        popup.setContentView(popupView);

        popup.setContentView(popupView);

        //obtenir la taille de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //la popup est un peu eloigné des bords de l'écran
        popup.setWidth((int) (screenWidth * 0.9));
        popup.setHeight((int) (screenHeight * 0.9));


        //sinon on voit la delimitation de la popup
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        //pour finir une petite animation d'apparition
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(500);
        popupView.startAnimation(fadeInAnimation);

        for(Evenement eve : fdb.getAllEvenement(getActivity())){
            System.out.print(typeuh.type + " " + eve.type);
            if(eve.type.equals(typeuh.type))
                ajouterEvenementDansPopup(popupView,eve);
        }

        Button bFermer = popupView.findViewById(R.id.boutonPopupListeEvenements);

        bFermer.setOnClickListener(osef-> {
            popup.dismiss();
            afficherAccueil();      //refresh la page
        });


    }

    public void ajouterEvenementDansPopup(View view, Evenement eve){

        LinearLayout parentLayout = view.findViewById(R.id.linearLayoutPopupAccueil);
        View vieweuh = LayoutInflater.from(getContext()).inflate(R.layout.evenements_cochable, parentLayout, false);



        TextView tv = vieweuh.findViewById(R.id.evenementNom);
        tv.setText(eve.nom);

        //change de couleur de fond pour avoir celui du type
        Drawable backgroundDrawableTV = tv.getBackground();
        backgroundDrawableTV.setColorFilter(Color.parseColor(fdb.getColorOfOneType(getActivity(), eve.type)), PorterDuff.Mode.SRC_ATOP);


        //je recupere l image et lui affecte un un on click effect mais aussi l image associé a si on a validé ou non
        ImageView iv = vieweuh.findViewById(R.id.evenementValide); //modifier cette ligne
        iv.setImageResource((eve.valide) ? R.drawable.tache_faite : R.drawable.tache_pas_faite);
        iv.setId(eve.id);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fdb.alterValideEvenement(getActivity(), eve.id, (eve.valide) ? 0 : 1);                             //modifie la valeur de valide dans la db
                iv.setImageResource((eve.valide) ? R.drawable.tache_pas_faite : R.drawable.tache_faite);       //modifie l'image a chaque clique
                eve.valide = (eve.valide) ? false : true;                                                       //modifie la valeur de valide de l'evenement


            }
        });

        //meme couleur de fond
        Drawable backgroundDrawableIV = iv.getBackground();
        backgroundDrawableIV.setColorFilter(Color.parseColor(fdb.getColorOfOneType(getActivity(), eve.type)), PorterDuff.Mode.SRC_ATOP);
        parentLayout.addView(vieweuh);
    }





    public void notifConnexion(){


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Notez que le mois commence à 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int cpt = 0;


        for(Evenement eve : fdb.getAllEvenement(getActivity())){
            if(eve.jour == day && eve.mois == month && eve.annee == year)
                cpt++;
        }



        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Nom du canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "channel_id")
                .setSmallIcon(R.drawable.tache_pas_faite)
                .setContentTitle("Titre de la notification")
                .setContentText(cpt + " évenement(s) aujourd'hui");

        notificationManager.notify(1, builder.build());





    }











    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_accueil, container, false);

        return rootView;

    }
}