package interfaces;

import model.BorneRecharge;
import model.Reservation;
import model.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface pour les services de gestion des réservations
 */
public interface ReservationService {
    
    /**
     * Recherche les bornes disponibles pour un créneau donné
     * @param dateDebut Date de début du créneau
     * @param dateFin Date de fin du créneau
     * @return Liste des bornes disponibles
     */
    List<BorneRecharge> rechercherBornesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    /**
     * Crée une nouvelle réservation
     * @param utilisateurId ID de l'utilisateur
     * @param borneId ID de la borne
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return La réservation créée ou null si échec
     */
    Reservation creerReservation(int utilisateurId, int borneId, LocalDateTime dateDebut, LocalDateTime dateFin);
    
    /**
     * Change le statut d'une réservation
     * @param reservationId ID de la réservation
     * @param statut Nouveau statut
     * @return true si modifiée avec succès, false sinon
     */
    boolean changerStatutReservation(int reservationId, StatutReservation statut);
    
    /**
     * Récupère toutes les réservations
     * @return Liste de toutes les réservations
     */
    List<Reservation> getAllReservations();
    
    /**
     * Récupère les réservations d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     */
    List<Reservation> getReservationsUtilisateur(int utilisateurId);
    
    /**
     * Récupère les réservations pour une borne
     * @param borneId ID de la borne
     * @return Liste des réservations pour cette borne
     */
    List<Reservation> getReservationsBorne(int borneId);
    
    /**
     * Vérifie si une borne a des réservations futures
     * @param borneId ID de la borne
     * @return true si la borne a des réservations futures, false sinon
     */
    boolean borneAReservationsFutures(int borneId);
    
    /**
     * Récupère une réservation par son ID
     * @param id ID de la réservation
     * @return La réservation ou null si non trouvée
     */
    Reservation getReservationById(int id);
} 