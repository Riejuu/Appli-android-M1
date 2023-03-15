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

            Context context = parent.getContext();
            typeItem = new TextView(context);
            typeItem.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            typeItem.setPadding(0, 20, 0, 20);
            typeItem.setTextSize(20);
            convertView = typeItem;

            //on fait un rectangle vert (code partiellement repris de CalendrierFragment
            float radius = context.getResources().getDisplayMetrics().density * 16;
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setStroke(1, Color.BLACK);

            shape.setColor(Color.GREEN);       //TODO CHANGER CETTE LIGNE PAR LA COULEUR DU TYPE QUAND CELA SERA FAIT
            typeItem.setBackground(shape);
            typeItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        } else {
            typeItem = (TextView) convertView;
        }

        // Mettre à jour le texte du TextView
        typeItem.setText(type);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childType = (String) getChild(groupPosition, childPosition);

        // Créer une nouvelle vue de layout si convertView est nul
        LinearLayout layout;
        if (convertView == null) {
            Context context = parent.getContext();
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(100, 0, 0, 0);

            TextView tiret = new TextView(context);
            tiret.setText("-");
            layout.addView(tiret);

            TextView childTypeItem = new TextView(context);
            childTypeItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            childTypeItem.setText(childType);
            layout.addView(childTypeItem);

            //Sinon il me les remets dans un ordre aleatoire
        } else {
            layout = (LinearLayout) convertView;
            ((TextView) layout.getChildAt(1)).setText(childType);
        }

        return layout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}



