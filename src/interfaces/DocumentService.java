package interfaces;

import model.Reservation;

/**
 * Interface pour les services de génération de documents
 */
public interface DocumentService {
    
    /**
     * Génère un reçu au format texte pour une réservation
     * @param reservation La réservation pour laquelle générer un reçu
     * @return true si génération réussie, false sinon
     */
    boolean genererRecu(Reservation reservation);
    
    /**
     * Génère un export des réservations
     * @return true si génération réussie, false sinon
     */
    boolean exporterHistorique();
} 