package com.example.taskmanagement;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListeTypesFragment extends Fragment {

    //fonction de la base de données
    FonctionsDatabase fdb = new FonctionsDatabase();

    //choix de couleur dans la popup
    TextView couleurTextView;
    SeekBar barreRouge;
    SeekBar barreVerte;
    SeekBar barreBleu;

    public ListeTypesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liste_types, container, false);
    }


    //quand la vue est affiché
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //on va créer un expandable list view, c est un dire un menu qui quand on clique dessus affiche autre chose
        //ici un menu de type et quand on clique sur le type, ca affiche les taches associées

        ExpandableListView expListView = getActivity().findViewById(R.id.expandableListView);
        expListView.removeAllViewsInLayout();       //remove au cas ou on reclique dessus

        List<String> typeListe = new ArrayList<>();
        Map<String, List<String>> dictionnaire = new HashMap<>();


        //pour cela on doit lister les parents et les enfant
        for (Types t : fdb.getAllTypes(getActivity())) {
            List<String> tachesListe = new ArrayList<>();
            typeListe.add(t.type);

            for(Evenement e : fdb.showTypeEvent(getActivity(), t.type))
                tachesListe.add(e.nom);

            dictionnaire.put(t.type,tachesListe);
        }

        //dans la classe MenuDeroulantType, il va s'occuper d'afficher les view parents et enfants
        MenuDeroulantTypes adapter = new MenuDeroulantTypes(getActivity(), typeListe, dictionnaire, getActivity());
        expListView.setAdapter(adapter);


        //l'image view peut faire un popup pour enregistrer les types

        ImageView iv = getActivity().findViewById(R.id.imageAjouterEnBasADroite);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherPopUpTypes(getView());
            }
        });



    }


    //la popup d'ajout d'un type
    //mis dans une fonction pour aerer le code
    public void afficherPopUpTypes(View view) {

        //besoin de true a la fin, sinon le clavier ne sortira pas quand on cliquera sur l edit text
        PopupWindow popup = new PopupWindow(view, view.getLayoutParams().WRAP_CONTENT,  view.getLayoutParams().WRAP_CONTENT, true);

        //on crée la popup a partir du xml enregistrer_types
        View popupView = getLayoutInflater().inflate(R.layout.popup_enregistrer_types, null);
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

        //sinon la variable globale reste null

        //on donne les fonctions aux barres pour la couleur dans la fenetre popup
        setUpCouleur(popupView);



        //le bouton enregistrer

        Button bEnregistrer = popupView.findViewById(R.id.boutonPopupEnregistrement);

        bEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText type = popupView.findViewById(R.id.nouveauType);


                //si les deux champs sont remplis (4 vu qu on a 4 barres en soit)
                if(!TextUtils.isEmpty(type.getText().toString().trim()) && !(barreRouge.getProgress() == 0 && barreVerte.getProgress() == 0 && barreBleu.getProgress() == 0) ){

                   //ajoute le type et la couleur en hexa (si existe deja, va faire comme si c etait rajouté du point de vu popup mais ne fera rien dans la db)
                    fdb.addTypes(getActivity(),type.getText().toString(), String.format("#%06X", (0xFFFFFF & ((ColorDrawable) couleurTextView.getBackground()).getColor())));
                    popup.dismiss();
                }else {
                    //si l edit text est vide, on coloris en rouge pour lui dire
                    if (TextUtils.isEmpty(type.getText().toString().trim())) {
                        System.out.println("type null");
                        TextView tvType = popupView.findViewById(R.id.nouveauTypeTV);
                        tvType.setTextColor(Color.RED);
                    }
                    //si les barre sont tous a 0 on colorie en rouge pour lui dire
                    if (barreRouge.getProgress() == 0 && barreVerte.getProgress() == 0 && barreBleu.getProgress() == 0) {
                        System.out.println("hexa null");
                        TextView tvCouleur = popupView.findViewById(R.id.couleurTV);
                        tvCouleur.setTextColor(Color.RED);
                    }
                }

            }
        });

        Button bFermer = popupView.findViewById(R.id.boutonPopupFermer);

        bFermer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            popup.dismiss();

            }

        });








    }


    public void setUpCouleur(View view){

        // on règle la couleur dans la vue popup
        couleurTextView = view.findViewById(R.id.color_preview);
        barreRouge = view.findViewById(R.id.red_seekbar);
        barreVerte = view.findViewById(R.id.green_seekbar);
        barreBleu = view.findViewById(R.id.blue_seekbar);

        barreRouge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCouleur();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        barreVerte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCouleur();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        barreBleu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCouleur();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }

    public void updateCouleur() {
        int red = barreRouge.getProgress();
        int green = barreVerte.getProgress();
        int blue = barreBleu.getProgress();
        int color = Color.rgb(red, green, blue);
        couleurTextView.setBackgroundColor(color);
    }











}