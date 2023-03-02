package com.example.taskmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BarreDesTaches extends Fragment {

    public BarreDesTaches() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_barre_des_taches, container, false);

        // Initialiser la barre de navigation
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.barreTacheImage1:
                        // Code pour changer vers la page d'acceuil
                        getActivity().setContentView(R.layout.activity_main);
                        return true;
                    case R.id.barreTacheImage2:
                        // Code pour changer vers la page 2
                        return true;
                    case R.id.barreTacheImage3:
                        // Code pour changer vers la page 3
                        return true;
                    case R.id.barreTacheImage4:
                        // Code pour changer vers la page calendrier
                        getActivity().setContentView(R.layout.calendrier);
                        return true;
                    case R.id.barreTacheImage5:
                        // Code pour changer vers la page 5
                        return true;
                    default:
                        return false;
                }
            }
        });

        return view;
    }
}