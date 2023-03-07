package com.example.taskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.taskmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //sert pour la barre de navigation
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  //content view changé pour avoir la barre des taches fonctionnelle
        replaceFragment(new AccueilFragment()); //ce met sur le fragment d'accueil de base


        //set l'image du curseur
        BottomNavigationView bottomNavigationView = findViewById(R.id.barreDesTaches);
        int middleItemId = R.id.barreTacheImage3;
        bottomNavigationView.setSelectedItemId(middleItemId);


        //attribue les pages aux boutons de la barre des taches
        binding.barreDesTaches.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.barreTacheImage1:
                    // Code pour changer vers la page parametres
                    replaceFragment(new ParametresFragment());
                    return true;
                case R.id.barreTacheImage2:
                    // Code pour changer vers la page 2
                    return true;
                case R.id.barreTacheImage3:
                    // Code pour changer vers la page d'accueil
                    replaceFragment(new AccueilFragment());
                    return true;
                case R.id.barreTacheImage4:
                    // Code pour changer vers la page calendrier
                    replaceFragment(new CalendrierFragment());
                    return true;
                case R.id.barreTacheImage5:
                    // Code pour changer vers la page 5
                    return true;
                default:
                    return false;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameMainActivity, fragment);
        fragmentTransaction.commit();
    }


}