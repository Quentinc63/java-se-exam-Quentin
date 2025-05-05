package services;

import interfaces.AuthentificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Utilisateur;

/**
 * Implémentation du service d'authentification
 */
public class AuthentificationServiceImpl implements AuthentificationService {
    
    private List<Utilisateur> utilisateurs;
    private Utilisateur utilisateurConnecte;
    private int nextId;
    
    public AuthentificationServiceImpl() {
        this.utilisateurs = new ArrayList<>();
        this.utilisateurConnecte = null;
        this.nextId = 1;
    }
    
    @Override
    public Utilisateur inscrire(String email, String motDePasse) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return null;
            }
        }
        
        String code = genererCode();
        Utilisateur nouvelUtilisateur = new Utilisateur(nextId++, email, motDePasse, code);
        utilisateurs.add(nouvelUtilisateur);
        
        System.out.println("\n=== EMAIL SIMULÉ ===");
        System.out.println("À: " + email);
        System.out.println("Sujet: Validation de votre compte Electricity Business");
        System.out.println("Votre code de validation est: " + code);
        System.out.println("====================\n");
        
        return nouvelUtilisateur;
    }
    
    @Override
    public boolean validerCompte(String email, String code) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getCodeValidation().equals(code)) {
                u.setEstValide(true);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Utilisateur connecter(String email, String motDePasse) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getMotDePasse().equals(motDePasse)) {
                if (!u.isEstValide()) {
                    System.out.println("Erreur: votre compte n'est pas encore validé.");
                    return null;
                }
                utilisateurConnecte = u;
                return u;
            }
        }
        return null;
    }
    
    @Override
    public void deconnecter() {
        utilisateurConnecte = null;
    }
    
    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * Génère un code de validation aléatoire à 6 chiffres
     */
    private String genererCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); 
        return String.valueOf(code);
    }
    
    /**
     * Récupère un utilisateur par son ID
     */
    public Utilisateur getUtilisateurById(int id) {
        for (Utilisateur u : utilisateurs) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }
} 