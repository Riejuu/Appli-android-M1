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
        public static final String COLUMN_VALIDE = "valide";
    }

    public static class TypesEntry implements BaseColumns {
        public static final String TABLE_NAME = "types";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COULEUR = "couleur";
    }


    //TABLE EVENEMENT

    public void addEvenement(Activity a, String _nom, String _type, int _annee, int _mois, int _jour,int _valide) {

        if(!typeExists(a,_type))
            addTypes(a, _type, "#AAAAAA");      //dans le cas ou on invoque la fonction pour un type qui n existe pas (ce n'est pas censé arriver si tout ce passe bien)

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", _nom);
        values.put("type", _type);
        values.put("annee", _annee);
        values.put("mois", _mois);
        values.put("jour", _jour);
        values.put("valide", _valide);
        long newRowId = db.insert("evenement", null, values);
    }

    //utile surtout pour le dev mais pas utilisé dans l appli
    public void showAllEvenement(Activity a){


        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_VALIDE
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
            boolean valide = cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_VALIDE)) == 1? true : false;
            System.out.println("id : "+id +" nom : "+ nom +" type: "+ type +" annee : "+ annee +" mois : "+ mois +" jour : "+ jour +" heure : "+ " valide : "+ valide);
        }

        cursor.close();
        db.close();
    }

    public ArrayList<Evenement> getAllEvenement(Activity a){


        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_VALIDE
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


        ArrayList<Evenement> listeEvenements = new ArrayList<>();
        while (cursor.moveToNext()) {
            // public Evenement(int _id, String _nom, String _type, int _annee, int _mois, int _jour, boolean _valide)
            Evenement osef = new Evenement(
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ANNEE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_MOIS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_JOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_VALIDE)) == 1? true : false);

            listeEvenements.add(osef);
        }
        cursor.close();
        db.close();
        return listeEvenements;

    }


    //récupère tous les evenements de la journée, et renvoie en ArrayList
    public ArrayList<Evenement> showDaysEvent(Activity a, int _annee, int _mois, int _jour){

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_VALIDE
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


        while (cursor.moveToNext()) {
           // public Evenement(int _id, String _nom, String _type, int _annee, int _mois, int _jour, boolean _valide)
            Evenement osef = new Evenement(
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ANNEE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_MOIS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_JOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_VALIDE)) == 1? true : false);

            listeEvenements.add(osef);
        }
        cursor.close();
        db.close();
        return listeEvenements;
    }

    //récupère tous les evenements du meme type
    public ArrayList<Evenement> showTypeEvent(Activity a, String _type){

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EvenementEntry.COLUMN_ID,
                EvenementEntry.COLUMN_NOM,
                EvenementEntry.COLUMN_TYPE,
                EvenementEntry.COLUMN_ANNEE,
                EvenementEntry.COLUMN_MOIS,
                EvenementEntry.COLUMN_JOUR,
                EvenementEntry.COLUMN_VALIDE
        };

        String selection = EvenementEntry.COLUMN_TYPE + " = ?";
        String[] arguments = {_type};

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


        while (cursor.moveToNext()) {
            // public Evenement(int _id, String _nom, String _type, int _annee, int _mois, int _jour, boolean _valide)
            Evenement osef = new Evenement(
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_ANNEE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_MOIS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_JOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EvenementEntry.COLUMN_VALIDE)) == 1? true : false);

            listeEvenements.add(osef);
        }
        cursor.close();
        db.close();
        return listeEvenements;
    }



    //modifie valide d'un evenement
    public void alterValideEvenement(Activity a, int id, int nouvelleValeur){

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("valide", nouvelleValeur);
        db.update("evenement", values, "id = ?", new String[] { String.valueOf(id) });
        db.close();

    }




    //efface un evenement selon son id
    public void deleteEvenement(Activity a, int id) {
        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("evenement", "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }







    // TABLE TYPES


    public boolean typeExists(Activity a, String type) {
        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM types WHERE type=?", new String[] { type });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public void addTypes(Activity a, String _type, String _couleur) {
        //verifie que le type n'existe pas deja
        if(!typeExists(a,_type)){
        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", _type);
        values.put("couleur", _couleur);
        long newRowId = db.insert("types", null, values);
        }
    }

    public void showAllTypes(Activity a){


        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TypesEntry.COLUMN_ID,
                TypesEntry.COLUMN_TYPE,
                TypesEntry.COLUMN_COULEUR,
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
            String couleur = cursor.getString(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_COULEUR));
            System.out.println("id : "+id +" type : "+ type +" couleur : " + couleur);
        }

        cursor.close();
        db.close();
    }

    //recupere toutes les entrées de la base de données Type
    public ArrayList<Types> getAllTypes(Activity a){

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TypesEntry.COLUMN_ID,
                TypesEntry.COLUMN_TYPE,
                TypesEntry.COLUMN_COULEUR
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

        ArrayList<Types> listeTypes = new ArrayList<>();

        while (cursor.moveToNext()) {
             Types osef = new Types(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_COULEUR)));

            listeTypes.add(osef);
        }


        cursor.close();
        db.close();
        return listeTypes;
    }

    public String getColorOfOneType(Activity a, String type) {

        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TypesEntry.COLUMN_ID,
                TypesEntry.COLUMN_TYPE,
                TypesEntry.COLUMN_COULEUR,
        };

        String selection = TypesEntry.COLUMN_TYPE + " = ?";
        String[] selectionArgs = { type };

        Cursor cursor = db.query(
                TypesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String couleur = "";

        if (cursor.moveToFirst()) {
            couleur = cursor.getString(cursor.getColumnIndexOrThrow(TypesEntry.COLUMN_COULEUR));
        }

        cursor.close();
        db.close();

        return couleur;
    }

    public void deleteTypes(Activity a,String type) {
        Database dbHelper = new Database(a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("evenement", "type = ?", new String[] { String.valueOf(type) });  //supprime le type de la table evenement
        db.delete("types", "type = ?", new String[] { String.valueOf(type) });      //supprime le type de la table type
        db.close();
    }


}
