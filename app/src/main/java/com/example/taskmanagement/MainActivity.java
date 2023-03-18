package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
        replaceFragment(new FragmentAccueil()); //ce met sur le fragment d'accueil de base


        //set l'image du curseur
        BottomNavigationView bottomNavigationView = findViewById(R.id.barreDesTaches);
        int middleItemId = R.id.barreTacheImage3;
        bottomNavigationView.setSelectedItemId(middleItemId);


        //je peux pas le rendre static du coup on va le recreer a chaque fois dans les fragments :(
        ImageView imageView = findViewById(R.id.imageAjouterEnBasADroite);

        //attribue les pages aux boutons de la barre des taches
        binding.barreDesTaches.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.barreTacheImage1:

                    // Code pour changer vers la page parametres
                    imageView.setVisibility(View.INVISIBLE);
                    replaceFragment(new FragmentParametres());
                    return true;


                case R.id.barreTacheImage2:



                    // Code pour changer vers la page liste des types
                    imageView.setVisibility(View.VISIBLE);
                    replaceFragment(new FragmentListeTypes());
                    return true;


                case R.id.barreTacheImage3:



                    // Code pour changer vers la page d'accueil
                    imageView.setVisibility(View.INVISIBLE);
                    replaceFragment(new FragmentAccueil());
                    return true;


                case R.id.barreTacheImage4:


                    // Code pour changer vers la page calendrier
                    imageView.setVisibility(View.INVISIBLE);
                    replaceFragment(new FragmentCalendrier());
                    return true;


                case R.id.barreTacheImage5:


                    // Code pour changer vers la page liste des evenements
                    imageView.setVisibility(View.VISIBLE);
                    replaceFragment(new FragmentListeEvenements());
                    return true;
                default:
                    return false;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentById(R.id.frameMainActivity);

        //rajout d une securité, car si on invoque deux fois les fragments, les elements inflaté n'existent plus
        if (existingFragment == null || !existingFragment.getClass().getName().equals(fragment.getClass().getName())) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameMainActivity, fragment);
            fragmentTransaction.commit();
        }
    }






}