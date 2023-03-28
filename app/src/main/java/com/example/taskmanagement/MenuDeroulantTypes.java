package com.example.taskmanagement;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class MenuDeroulantTypes extends BaseExpandableListAdapter {

    public Context context;
    public List<String> typeListe;
    public Map<String, List<Evenement>> dictionnaire;



    FonctionsDatabase fdb = new FonctionsDatabase();

    //rajout de l activity pour pouvoir recuperer l activité des types
    public Activity activity ;

    //besoin de ceci pour la popup view fille
    private LayoutInflater layoutInflater;
    private PopupWindow popup;

    //besoin pour refresh la page quand on ferme le popup
    public FragmentListeTypes fragment;
    ExpandableListView ela;


    public MenuDeroulantTypes(Context context, List<String> typeListe, Map<String, List<Evenement>> dictionnaire, Activity activity, FragmentListeTypes fragment) {
        this.context = context;
        this.typeListe = typeListe;
        this.dictionnaire = dictionnaire;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return typeListe.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String type = typeListe.get(groupPosition);
        List<Evenement> tachesListe = dictionnaire.get(type);
        return tachesListe.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return typeListe.get(groupPosition);
    }

    @Override
    public Evenement getChild(int groupPosition, int childPosition) {
        String type = typeListe.get(groupPosition);
        List<Evenement> tachesListe = dictionnaire.get(type);
        return tachesListe.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String type = (String) getGroup(groupPosition);
        TextView typeItem;
        if (convertView == null) {
            // Utiliser LayoutInflater pour charger la vue de groupe à partir du fichier XML
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.liste_type_mere, null);
        }

        // Mettre à jour le texte du TextView
        typeItem = (TextView) convertView.findViewById(R.id.typesItem);
        typeItem.setText(type);

        //change la couleur pour l'adapter a celle de la db
        Drawable backgroundDrawable = typeItem.getBackground();

        backgroundDrawable.setColorFilter(Color.parseColor(fdb.getColorOfOneType(activity,type)), PorterDuff.Mode.SRC_ATOP);
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //si la vue n existe pas, on la crée
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.liste_type_fille, parent, false);
        }


        String childItem = getChild(groupPosition, childPosition).nom;

        // On met à jour la vue avec les données de l'élément enfant
        TextView childTextView = convertView.findViewById(R.id.itemFille);
        childTextView.setText(childItem);

        //on set l'id, sera utile pour récuperer l'event dans une base de données
        convertView.setId(getChild(groupPosition, childPosition).id);


        View tmp = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JE SUIS ICI", "dans le onclick");
                Log.d("JE SUIS ICI",childTextView.getText() + String.valueOf(tmp.getId()));

                afficherPopUpModifierEvenements(tmp, fdb.getEvenementById(activity, tmp.getId()));

                Log.d("JE SUIS ICI", "en dehors du onclick");
            }
        });

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }






    public void afficherPopUpModifierEvenements(View view, Evenement eve) {

        // Initialiser LayoutInflater et créer une vue de popup personnalisée
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View popupView = layoutInflater.inflate(R.layout.popup_enregisrer_et_modifier_evenements, null);


        // Créer une popup
        popup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        //obtenir la taille de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //la popup est un peu eloigné des bords de l'écran
        popup.setWidth((int) (screenWidth * 0.9));
        popup.setHeight((int) (screenHeight * 0.9));


        // Rendre la popup transparente
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Afficher la popup
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Définir l'animation de la popup
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        popupView.startAnimation(alphaAnimation);




        //maintenant on setup les editTexts et spinner puis on leur donne par défaut la valeur de l'item


        //spinner du type
        Spinner typeSpinner = popupView.findViewById(R.id.evenementType);

        //je dois utiliser une liste classique pour les spinner donc je change l array en liste normale

        ArrayList<String> listeTypeArray = new ArrayList<>();

        for(Types t : fdb.getAllTypes(activity)){
            listeTypeArray.add(t.type);
        }

        String[] listeType = listeTypeArray.toArray(new String[0]);



        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, listeType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setSelection(typeAdapter.getPosition(eve.type));

        EditText nom = popupView.findViewById(R.id.evenementNom);


        nom.setText(eve.nom);


        DatePicker dp = popupView.findViewById(R.id.datePicker);
        dp.init(eve.annee, eve.mois, eve.jour, null);

        //String date = jour + "/" + mois + "/" + annee;



        //et pour finir les boutons enregistrer et fermer
        Button bEnregistrer = popupView.findViewById(R.id.boutonPopupEnregistrementOuModificationEvenement);

        bEnregistrer.setOnClickListener(osef -> {


            if(!TextUtils.isEmpty(nom.getText().toString().trim()) && !(typeSpinner.getSelectedItem() == null)){

                //on ajoute a la db
                fdb.alterEvenementFromId(activity,
                        eve.id,
                        nom.getText().toString(),
                        typeSpinner.getSelectedItem().toString(),
                        dp.getYear(),
                        (dp.getMonth() + 1),    // Ajouter 1 car les mois commencent à 0
                        dp.getDayOfMonth());

                popup.dismiss();
                Toast.makeText(activity,"Evenement modifié avec succès", Toast.LENGTH_SHORT).show();
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




            }



        });

        Button bFermer = popupView.findViewById(R.id.boutonPopupFermerEvenement);
        bFermer.setText("Supprimer");
        bFermer.setOnClickListener(osef-> {
            fdb.deleteEvenement(activity, eve.id);



            fragment.afficherType();







            popup.dismiss();
        });















    }

}