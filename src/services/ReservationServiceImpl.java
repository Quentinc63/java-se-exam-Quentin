package services;

import interfaces.ReservationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.BorneRecharge;
import model.EtatBorne;
import model.Reservation;
import model.StatutReservation;
import model.Utilisateur;

/**
 * Implémentation du service de gestion des réservations
 */
public class ReservationServiceImpl implements ReservationService {
    
    private List<Reservation> reservations;
    private int nextReservationId;
    private AuthentificationServiceImpl authentificationService;
    private BorneServiceImpl borneService;
    
    public ReservationServiceImpl(AuthentificationServiceImpl authentificationService, 
                                 BorneServiceImpl borneService) {
        this.reservations = new ArrayList<>();
        this.nextReservationId = 1;
        this.authentificationService = authentificationService;
        this.borneService = borneService;
    }
    
    @Override
    public List<BorneRecharge> rechercherBornesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<BorneRecharge> bornesDisponibles = new ArrayList<>();
        for (var lieu : borneService.getAllLieux()) {
            for (BorneRecharge borne : lieu.getBornes()) {
                if (borne.getEtat() == EtatBorne.DISPONIBLE && !estReservee(borne, dateDebut, dateFin)) {
                    bornesDisponibles.add(borne);
                }
            }
        }
        
        return bornesDisponibles;
    }
    
    /**
     * Vérifie si une borne est déjà réservée sur une période donnée
     */
    private boolean estReservee(BorneRecharge borne, LocalDateTime dateDebut, LocalDateTime dateFin) {
        for (Reservation reservation : reservations) {
            if (reservation.getBorne().getId() == borne.getId() && 
                reservation.getStatut() != StatutReservation.REFUSEE) {
                
                if ((dateDebut.isBefore(reservation.getDateFin()) || dateDebut.isEqual(reservation.getDateFin())) && 
                    (dateFin.isAfter(reservation.getDateDebut()) || dateFin.isEqual(reservation.getDateDebut()))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Reservation creerReservation(int utilisateurId, int borneId, 
                                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            return null;
        }
        
        Utilisateur utilisateur = authentificationService.getUtilisateurById(utilisateurId);
        BorneRecharge borne = borneService.getBorneById(borneId);
        
        if (utilisateur == null || borne == null) {
            return null;
        }
        
        if (estReservee(borne, dateDebut, dateFin)) {
            return null;
        }
        Reservation reservation = new Reservation(nextReservationId++, utilisateur, borne, dateDebut, dateFin);
        reservations.add(reservation);
        
        return reservation;
    }
    
    @Override
    public boolean changerStatutReservation(int reservationId, StatutReservation statut) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }
        
        reservation.setStatut(statut);
        return true;
    }
    
    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
    
    @Override
    public List<Reservation> getReservationsUtilisateur(int utilisateurId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getUtilisateur().getId() == utilisateurId) {
                result.add(reservation);
            }
        }
        return result;
    }
    
    @Override
    public List<Reservation> getReservationsBorne(int borneId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getBorne().getId() == borneId) {
                result.add(reservation);
            }
        }
        return result;
    }
    
    @Override
    public boolean borneAReservationsFutures(int borneId) {
        LocalDateTime maintenant = LocalDateTime.now();
        
        for (Reservation reservation : reservations) {
            if (reservation.getBorne().getId() == borneId && 
                reservation.getStatut() != StatutReservation.REFUSEE &&
                reservation.getDateFin().isAfter(maintenant)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public Reservation getReservationById(int id) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }
} 