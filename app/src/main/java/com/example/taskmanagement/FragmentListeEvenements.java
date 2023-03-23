package com.example.taskmanagement;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        // Inflate the layout for this fragment>
        return inflater.inflate(R.layout.fragment_liste_evenements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        for(Evenement e : fdb.getAllEvenement(getActivity()))
            afficherEvenement(e);


        ImageView iv = getActivity().findViewById(R.id.imageAjouterEnBasADroite);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherPopUpAjouterEvenements(getView());
            }
        });

    }

    public void afficherEvenement(Evenement eve){

        LinearLayout parentLayout = getActivity().findViewById(R.id.linearLayoutEvenements);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.liste_evenement_evenement, parentLayout, false);

        TextView tv = view.findViewById(R.id.listeEvenementsEvenement);
        tv.setText(eve.nom);

        Drawable backgroundDrawable = tv.getBackground();
        backgroundDrawable.setColorFilter(Color.parseColor(fdb.getColorOfOneType(getActivity(),eve.type)), PorterDuff.Mode.SRC_ATOP);

        parentLayout.addView(view);

    }


    public void afficherPopUpAjouterEvenements(View view) {

        PopupWindow popup = new PopupWindow(view, view.getLayoutParams().WRAP_CONTENT,  view.getLayoutParams().WRAP_CONTENT, true);

        //on crée la popup a partir du xml enregistrer_types
        View popupView = getLayoutInflater().inflate(R.layout.popup_enregisrer_et_modifier_evenements, null);
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


        //ensuite on s'occupe de setup les spinner selon les infos de la base de données + les dates possibles

        //spinner du type
        Spinner typeSpinner = popupView.findViewById(R.id.evenementType);

        //je dois utiliser une liste classique pour les spinner donc je change l array en liste normale

        ArrayList<String> listeTypeArray = new ArrayList<>();

        for(Types t : fdb.getAllTypes(getActivity())){
            listeTypeArray.add(t.type);
        }

        String[] listeType = listeTypeArray.toArray(new String[0]);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, listeType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);










        //les spinner de la date
        Spinner anneeSpinner = popupView.findViewById(R.id.evenementAnnee);
        Spinner moisSpinner = popupView.findViewById(R.id.evenementMois);
        Spinner jourSpinner = popupView.findViewById(R.id.evenementJour);


        //on les initialises
        String[] listeAnnees = new String[130];
        for (int i = 0; i < 130; i++) {
            listeAnnees[i] = String.valueOf(i+1970);  //pour les années 1970 a 2099, peut etre que ca peut etre utile a certain de note des evenements passer comme pense bete
        }


        String[] listeMois = new String[12];
        for (int i = 0; i < 12; i++) {
            listeMois[i] = String.valueOf(i+1);  //pour les mois
        }


        String[] listeJours = new String[31];
        for (int i = 0; i < 31; i++) {
            listeJours[i] = String.valueOf(i+1);  //pour les jours (on verifie si c est possible dans le bouton enregistrer)
        }




        adapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, listeAnnees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anneeSpinner.setAdapter(adapter);
        anneeSpinner.setSelection(adapter.getPosition(String.valueOf(2023)));

        adapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, listeMois);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moisSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, listeJours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jourSpinner.setAdapter(adapter);



        //et pour finir les boutons enregistrer et fermer


        Button bEnregistrer = popupView.findViewById(R.id.boutonPopupEnregistrementOuModificationEvenement);

        bEnregistrer.setOnClickListener(osef -> {

            EditText nom = popupView.findViewById(R.id.evenementNom);
            /*Spinner typeSpinner = popupView.findViewById(R.id.evenementType);
            Spinner anneeSpinner = popupView.findViewById(R.id.evenementAnnee);
            Spinner moisSpinner = popupView.findViewById(R.id.evenementMois);
            Spinner jourSpinner = popupView.findViewById(R.id.evenementJour);
            */


            if(!TextUtils.isEmpty(nom.getText().toString().trim()) && dateValide(jourSpinner, moisSpinner, anneeSpinner) && !(typeSpinner.getSelectedItem() == null)){

                //on ajoute a la db
                fdb.addEvenement(getActivity(),nom.getText().toString(), typeSpinner.getSelectedItem().toString(),
                        Integer.parseInt(anneeSpinner.getSelectedItem().toString()),
                        Integer.parseInt(moisSpinner.getSelectedItem().toString()),
                        Integer.parseInt(jourSpinner.getSelectedItem().toString()),
                        0);

                popup.dismiss();
            }else{



                if(TextUtils.isEmpty(nom.getText().toString().trim()) ){
                    TextView tvType = popupView.findViewById(R.id.nouveauEvenementNomTV);
                    tvType.setTextColor(Color.RED);
                }


                //ce if est utile uniquement dans le cas ou on vient d installer l appli et que nous avons aucun type d enregistré pour le moment
                if(typeSpinner.getSelectedItem() == null){
                    TextView tvType = popupView.findViewById(R.id.nouveauEvenementTypeTV);
                    tvType.setTextColor(Color.RED);
                }

                if(!dateValide(jourSpinner, moisSpinner, anneeSpinner)){
                    TextView tvType = popupView.findViewById(R.id.nouveauEvenementDateTV);
                    tvType.setTextColor(Color.RED);
                }


            }



        });

        Button bFermer = popupView.findViewById(R.id.boutonPopupFermerEvenement);

        bFermer.setOnClickListener(osef-> popup.dismiss());








    }

    public boolean dateValide(Spinner daySpinner, Spinner monthSpinner, Spinner yearSpinner) {
        // Récupération des valeurs sélectionnées dans les Spinners

        //si les mois sont inferieur a 10 alors on rajoute un 0, exemple 9 -> 09
        String day = (Integer.parseInt(daySpinner.getSelectedItem().toString()) >9)? daySpinner.getSelectedItem().toString() : "0"+daySpinner.getSelectedItem().toString();
        String month =(Integer.parseInt(monthSpinner.getSelectedItem().toString()) >9)? monthSpinner.getSelectedItem().toString() : "0"+monthSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        // Concaténation des valeurs pour former la chaîne de date
        String dateString =  day + "/" + month + "/" + year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateString);
            return true;
        } catch (ParseException e) {
            // Si la conversion échoue, la date n'est pas valide
            return false;
        }
    }





}