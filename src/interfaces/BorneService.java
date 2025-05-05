package interfaces;

import model.BorneRecharge;
import model.LieuRecharge;

import java.util.List;

/**
 * Interface pour les services de gestion des bornes et lieux
 */
public interface BorneService {
    
    /**
     * Ajoute un nouveau lieu de recharge
     * @param nom Nom du lieu
     * @param adresse Adresse du lieu
     * @return Le lieu créé
     */
    LieuRecharge ajouterLieu(String nom, String adresse);
    
    /**
     * Modifie un lieu existant
     * @param lieuId ID du lieu à modifier
     * @param nom Nouveau nom
     * @param adresse Nouvelle adresse
     * @return true si modifié avec succès, false sinon
     */
    boolean modifierLieu(int lieuId, String nom, String adresse);
    
    /**
     * Récupère tous les lieux
     * @return Liste de tous les lieux
     */
    List<LieuRecharge> getAllLieux();
    
    /**
     * Récupère un lieu par son ID
     * @param id ID du lieu
     * @return Le lieu ou null si non trouvé
     */
    LieuRecharge getLieuById(int id);
    
    /**
     * Ajoute une borne à un lieu
     * @param lieuId ID du lieu
     * @param tarifHoraire Tarif horaire de la borne
     * @return La borne créée ou null si échec
     */
    BorneRecharge ajouterBorne(int lieuId, double tarifHoraire);
    
    /**
     * Modifie le tarif d'une borne
     * @param borneId ID de la borne
     * @param nouveauTarif Nouveau tarif horaire
     * @return true si modifié avec succès, false sinon
     */
    boolean modifierTarifBorne(int borneId, double nouveauTarif);
    
    /**
     * Supprime une borne
     * @param borneId ID de la borne à supprimer
     * @return true si supprimée avec succès, false sinon
     */
    boolean supprimerBorne(int borneId);
    
    /**
     * Récupère une borne par son ID
     * @param id ID de la borne
     * @return La borne ou null si non trouvée
     */
    BorneRecharge getBorneById(int id);
} 