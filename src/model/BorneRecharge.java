package model;

/**
 * Représente une borne de recharge
 */
public class BorneRecharge {
    private int id;
    private EtatBorne etat;
    private double tarifHoraire;

    public BorneRecharge(int id, double tarifHoraire) {
        this.id = id;
        this.etat = EtatBorne.DISPONIBLE;
        this.tarifHoraire = tarifHoraire;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public EtatBorne getEtat() {
        return etat;
    }

    public void setEtat(EtatBorne etat) {
        this.etat = etat;
    }

    public double getTarifHoraire() {
        return tarifHoraire;
    }

    public void setTarifHoraire(double tarifHoraire) {
        this.tarifHoraire = tarifHoraire;
    }

    @Override
    public String toString() {
        return "Borne #" + id + " (" + etat + ") - " + tarifHoraire + "€/h";
    }
} 