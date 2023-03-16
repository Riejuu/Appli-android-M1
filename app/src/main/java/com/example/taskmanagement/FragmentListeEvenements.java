package com.example.taskmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentListeEvenements extends Fragment {

    //fonction de la base de données
    FonctionsDatabase fdb = new FonctionsDatabase();


    public FragmentListeEvenements() {
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
        return inflater.inflate(R.layout.fragment_liste_evenements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        for(Evenement e : fdb.getAllEvenement(getActivity()))
            afficherEvenementCalendrier(e);

    }

    public void afficherEvenementCalendrier(Evenement eve){

        LinearLayout parentLayout = getActivity().findViewById(R.id.linearLayoutEvenements);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.liste_evenement_evenement, parentLayout, false);

        TextView tv = view.findViewById(R.id.listeEvenementsEvenement);
        tv.setText(eve.nom);

        parentLayout.addView(view);


    }





}