package com.example.taskmanagement;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListeTypesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeTypesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FonctionsDatabase fdb = new FonctionsDatabase();

    public ListeTypesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListeTypes.
     */
    // TODO: Rename and change types and number of parameters
    public static ListeTypesFragment newInstance(String param1, String param2) {
        ListeTypesFragment fragment = new ListeTypesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


        afficherTypes();


    }


    public void afficherTypes(){


        //on va créer un expandable list view, c est un dire un menu qui quand on clique dessus affiche autre chose
        //ici un menu de type et quand on clique sur le type, ca affiche les taches associées

        //pour cela on doit lister les parents et les enfant



        ExpandableListView expListView = getActivity().findViewById(R.id.expandableListView);
        expListView.removeAllViewsInLayout();       //remove au cas ou on reclique dessus

        List<String> typeListe = new ArrayList<>();
        Map<String, List<String>> dictionnaire = new HashMap<>();


        for (Types t : fdb.getAllTypes(getActivity())) {
            List<String> tachesListe = new ArrayList<>();
            typeListe.add(t.type);

            for(Evenement e : fdb.showTypeEvent(getActivity(), t.type))
                tachesListe.add(e.nom);

            dictionnaire.put(t.type,tachesListe);
        }

        System.out.println(dictionnaire);

        MenuDeroulantTypes adapter = new MenuDeroulantTypes(getActivity(), typeListe, dictionnaire);
        expListView.setAdapter(adapter);

    }





}