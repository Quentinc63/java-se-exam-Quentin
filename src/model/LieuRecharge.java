package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un lieu de recharge contenant plusieurs bornes
 */
public class LieuRecharge {
    private int id;
    private String nom;
    private String adresse;
    private List<BorneRecharge> bornes;

    public LieuRecharge(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.bornes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<BorneRecharge> getBornes() {
        return bornes;
    }

    public void ajouterBorne(BorneRecharge borne) {
        bornes.add(borne);
    }

    public void supprimerBorne(BorneRecharge borne) {
        bornes.remove(borne);
    }

    public BorneRecharge getBorneById(int id) {
        for (BorneRecharge borne : bornes) {
            if (borne.getId() == id) {
                return borne;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Lieu #" + id + " : " + nom + " (" + adresse + ") - " + bornes.size() + " bornes";
    }
} 