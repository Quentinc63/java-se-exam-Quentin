package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente une réservation de borne par un utilisateur
 */
public class Reservation {
    private int id;
    private Utilisateur utilisateur;
    private BorneRecharge borne;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutReservation statut;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Reservation(int id, Utilisateur utilisateur, BorneRecharge borne, 
                      LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.borne = borne;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = StatutReservation.EN_ATTENTE;
    }

    public int getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public BorneRecharge getBorne() {
        return borne;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public double calculerPrixTotal() {
        long heures = dateDebut.until(dateFin, java.time.temporal.ChronoUnit.HOURS);
        return heures * borne.getTarifHoraire();
    }

    @Override
    public String toString() {
        return "Réservation #" + id + " : " + 
                utilisateur.getEmail() + " - Borne #" + borne.getId() + 
                " - Du " + dateDebut.format(formatter) + 
                " au " + dateFin.format(formatter) + 
                " - Statut : " + statut;
    }
} 