package com.example.taskmanagement;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListeTypesFragment extends Fragment {

    FonctionsDatabase fdb = new FonctionsDatabase();

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
        MenuDeroulantTypes adapter = new MenuDeroulantTypes(getActivity(), typeListe, dictionnaire);
        expListView.setAdapter(adapter);

        afficherPopUpTypes(getView(), MainActivity.popup);

    }


    //la popup d'ajout d'un type
    public void afficherPopUpTypes(View view, PopupWindow popup)
    {
        popup = new PopupWindow(getActivity());
        //on crée la popup a partir du xml enregistrer_types
        View popupView = getLayoutInflater().inflate(R.layout.enregistrer_types, null);
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
        MainActivity.popup = popup;

    }












}