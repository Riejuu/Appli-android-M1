package com.example.taskmanagement;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class MenuDeroulantTypes extends BaseExpandableListAdapter {

    public Context context;
    public List<String> typeListe;
    public Map<String, List<String>> dictionnaire;

    public MenuDeroulantTypes(Context context, List<String> typeListe, Map<String, List<String>> dictionnaire) {
        this.context = context;
        this.typeListe = typeListe;
        this.dictionnaire = dictionnaire;
    }

    @Override
    public int getGroupCount() {
        return typeListe.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String type = typeListe.get(groupPosition);
        List<String> tachesListe = dictionnaire.get(type);
        return tachesListe.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return typeListe.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String type = typeListe.get(groupPosition);
        List<String> tachesListe = dictionnaire.get(type);
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
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.GREEN);                          //TODO CHANGER CETTE LIGNE PAR LA COULEUR DU TYPE QUAND CELA SERA FAIT

        typeItem.setBackground(drawable);
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //si la vue n existe pas, on la crée
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.liste_type_fille, parent, false);
        }


        String childItem = (String) getChild(groupPosition, childPosition);

        // On met à jour la vue avec les données de l'élément enfant
        TextView childTextView = convertView.findViewById(R.id.itemFille);
        childTextView.setText(childItem);

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}



