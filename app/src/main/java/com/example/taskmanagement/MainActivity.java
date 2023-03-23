package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.taskmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.imageAjouterEnBasADroite);

        setupViewPager();
        setupBottomNavigation();
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new FragmentParametres());
        pagerAdapter.addFragment(new FragmentListeTypes());
        pagerAdapter.addFragment(new FragmentAccueil());
        pagerAdapter.addFragment(new FragmentCalendrier());
        pagerAdapter.addFragment(new FragmentListeEvenements());

        viewPager.setAdapter(pagerAdapter);

        //dit que de base on est sur la page accueil
        viewPager.setCurrentItem(2);
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.barreDesTaches);

        // set la barre de navigation pour etre lié a la nav barre
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.barreTacheImage1:

                    // Code pour changer vers la page parametres
                    imageView.setVisibility(View.INVISIBLE);
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.barreTacheImage2:

                    // Code pour changer vers la page liste des types
                    imageView.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.barreTacheImage3:

                    // Code pour changer vers la page d'accueil
                    imageView.setVisibility(View.INVISIBLE);
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.barreTacheImage4:

                    // Code pour changer vers la page calendrier
                    imageView.setVisibility(View.INVISIBLE);
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.barreTacheImage5:

                    // Code pour changer vers la page liste des evenements
                    imageView.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(4);
                    return true;
                default:
                    return false;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                if (position == 1 || position == 4) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }





}
