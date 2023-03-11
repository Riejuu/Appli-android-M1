package com.example.taskmanagement;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;


//cette classe sert a interroger la base de donnée
public class FonctionsDatabase {

    public FonctionsDatabase() {

    }

    public static class EvenementEntry implements BaseColumns {
        public static final String TABLE_NAME = "evenement";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOM = "nom";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_ANNEE = "annee";
        public static final String COLUMN_MOIS = "mois";
        public static final String COLUMN_JOUR = "jour";
        public static final String COLUMN_HEURE = "heure";
        public static final String COLUMN_MINUTE = "minute";
    }

    public static class TypesEntry implements BaseColumns {
        public static final String TABLE_NAME = "types";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
    }


    //TABLE EVENEMENT

    public void addEvenement(Activity a, String _nom, String _type, int _annee, int _mois, int _jour, int _heure, int _minute) {

        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", _nom);
        values.put("type", _type);
        values.put("annee", _annee);
        values.put("mois", _mois);
        values.put("jour", _jour);
        values.put("heure", _heure);
        values.put("minute", _minute);
        long newRowId = db.insert("evenement", null, values);
    }

    //utile surtout pour le dev mais pas utilisé dans l appli
    public void showAllEvenement(Activity a){


        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_HEURE,
                EvenementEntry.COLUMN_MINUTE
        };

        Cursor cursor = db.query(
                EvenementEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_NOM));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_TYPE));
            int annee = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ANNEE));
            int jour = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_JOUR));
            int mois = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_MOIS));
            int heure = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_HEURE));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_MINUTE));
            System.out.println("id : "+id +" nom : "+ nom +" type: "+ type +" annee : "+ annee +" mois : "+ mois +" jour : "+ jour +" heure : "+ heure +" minute : "+ minute);
        }

        cursor.close();
        db.close();
    }

    //récupère tous les evenements de la journée, et renvoie en ArrayList
    public ArrayList<Evenement> showDaysEvent(Activity a, int _annee, int _mois, int _jour){

        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_HEURE,
                EvenementEntry.COLUMN_MINUTE
        };

        String selection = EvenementEntry.COLUMN_ANNEE + " = ? AND " +
                EvenementEntry.COLUMN_MOIS + " = ? AND " +
                EvenementEntry.COLUMN_JOUR + " = ?";
        String[] arguments = {String.valueOf(_annee), String.valueOf(_mois),String.valueOf(_jour)};

        Cursor cursor = db.query(
                EvenementEntry.TABLE_NAME,
                projection,
                selection,
                arguments,
                null,
                null,
                null
        );

        ArrayList<Evenement> listeEvenements = new ArrayList<>();

        Evenement osef = new Evenement();

        while (cursor.moveToNext()) {
            osef.nom = cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_NOM));
            listeEvenements.add(osef);
        }
        cursor.close();
        db.close();
        return listeEvenements;
    }


    //récupère un evenement en particulier




    //efface un evenement selon son id
    public void deleteEvenement(Activity a, int id) {
        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("evenement", "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }







    // TABLE TYPES


    public boolean typeExists(Activity a, String type) {
        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM types WHERE type=?", new String[] { type });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }


    public void addTypes(Activity a, String _type) {
        //verifie que le type n'existe pas deja
        if(typeExists(a,_type)){
        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", _type);
        long newRowId = db.insert("types", null, values);
        }
    }

    public void showAllTypes(Activity a){


        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TypesEntry.COLUMN_ID,
                TypesEntry.COLUMN_TYPE,
        };

        Cursor cursor = db.query(
                TypesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_ID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_TYPE));
            System.out.println("id : "+id +" type: "+ type);
        }

        cursor.close();
        db.close();
    }

    public void deleteTypes(Activity a,String type) {
        EvenementDatabase dbHelper = new EvenementDatabase(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("evenement", "type = ?", new String[] { String.valueOf(type) });  //supprime le type de la table evenement
        db.delete("types", "type = ?", new String[] { String.valueOf(type) });      //supprime le type de la table type
        db.close();
    }


}
