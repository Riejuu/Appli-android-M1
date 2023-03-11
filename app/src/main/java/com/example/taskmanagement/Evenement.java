package com.example.taskmanagement;

public class Evenement {


    public int id ;
    public String nom ;
    public String type;
    public int annee;
    public int mois;
    public int jour;
    public int heure;
    public int minute;

    public Evenement(){
    }

    public Evenement(int _id, String _nom, String _type, int _annee, int _mois, int _jour, int _heure, int _minute) {
        id = _id;
        nom = _nom;
        type = _type;
        annee = _annee;
        mois = _mois;
        jour = _jour;
        heure = _heure;
        minute = _minute;
    }
}
