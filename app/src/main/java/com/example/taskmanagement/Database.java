package com.example.taskmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "evenement.db";
    private static final int DATABASE_VERSION = 1;

    String createTable = "CREATE TABLE evenement ("         //table des evenements du calendrier
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nom TEXT, "
            + "type TEXT, "
            + "annee INTEGER, "
            + "mois INTEGER, "
            + "jour INTEGER, "
            + "valide INTEGER DEFAULT 0)";

    String createTypes = "CREATE TABLE types ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "      //table pour stocker les types de taches, j'aurais pu lier une dépendance entre les tables pour rajouter de la securité cependant j'ai préféré me concentrer sur la partie android plutot que base de donnée
            + "type TEXT,"
            + " couleur TEXT)";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
        db.execSQL(createTypes);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS evenement");
        db.execSQL("DROP TABLE IF EXISTS types");
        onCreate(db);
    }
}