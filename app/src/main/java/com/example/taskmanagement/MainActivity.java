package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.taskmanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

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

        //dit que de base on est sur la page accueil
        viewPager.setCurrentItem(1);







        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Nom du canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.tache_pas_faite)
                .setContentTitle("Titre de la notification")
                .setContentText("Contenu de la notification");



        notificationManager.notify(1, builder.build());





    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new FragmentListeTypes());
        pagerAdapter.addFragment(new FragmentAccueil());
        pagerAdapter.addFragment(new FragmentCalendrier());
        pagerAdapter.addFragment(new FragmentListeEvenements());


        viewPager.setAdapter(pagerAdapter);


    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.barreDesTaches);

        // set la barre de navigation pour etre lié a la nav barre
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.barreTacheImage1:

                    // Code pour changer vers la page liste des types
                    imageView.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.barreTacheImage2:

                    // Code pour changer vers la page d'accueil
                    imageView.setVisibility(View.INVISIBLE);
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.barreTacheImage3:

                    // Code pour changer vers la page calendrier
                    imageView.setVisibility(View.INVISIBLE);
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.barreTacheImage4:

                    // Code pour changer vers la page liste des evenements
                    imageView.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(3);
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
                if (position == 0 || position == 3) {
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
