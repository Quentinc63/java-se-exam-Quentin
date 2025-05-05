package interfaces;

import model.Utilisateur;

/**
 * Interface pour les services d'authentification
 */
public interface AuthentificationService {
    
    /**
     * Inscrit un nouvel utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return L'utilisateur créé ou null si l'email est déjà utilisé
     */
    Utilisateur inscrire(String email, String motDePasse);
    
    /**
     * Valide un compte utilisateur avec le code de validation
     * @param email Email de l'utilisateur
     * @param code Code de validation
     * @return true si la validation a réussi, false sinon
     */
    boolean validerCompte(String email, String code);
    
    /**
     * Connecte un utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return L'utilisateur connecté ou null si échec de connexion
     */
    Utilisateur connecter(String email, String motDePasse);
    
    /**
     * Déconnecte l'utilisateur actuel
     */
    void deconnecter();
    
    /**
     * Récupère l'utilisateur actuellement connecté
     * @return L'utilisateur connecté ou null si aucun
     */
    Utilisateur getUtilisateurConnecte();
} 