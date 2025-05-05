package ui;

import interfaces.AuthentificationService;
import interfaces.BorneService;
import interfaces.DocumentService;
import interfaces.ReservationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.BorneRecharge;
import model.LieuRecharge;
import model.Reservation;
import model.StatutReservation;
import model.Utilisateur;

/**
 * Gestion des menus de l'application
 */
public class Menu {
    private Scanner scanner;
    private AuthentificationService authentificationService;
    private BorneService borneService;
    private ReservationService reservationService;
    private DocumentService documentService;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public Menu(AuthentificationService authentificationService, 
               BorneService borneService,
               ReservationService reservationService,
               DocumentService documentService) {
        this.scanner = new Scanner(System.in);
        this.authentificationService = authentificationService;
        this.borneService = borneService;
        this.reservationService = reservationService;
        this.documentService = documentService;
    }
    
    /**
     * Affiche le menu principal et gère les interactions
     */
    public void afficherMenuPrincipal() {
        boolean quitter = false;
        
        while (!quitter) {
            System.out.println("\n=== Electricity Business ===");
            System.out.println("1. S'inscrire");
            System.out.println("2. Valider l'inscription");
            System.out.println("3. Se connecter");
            System.out.println("4. Rechercher & réserver une borne");
            System.out.println("5. Gérer mes réservations");
            System.out.println("6. Administration (lieux / bornes)");
            System.out.println("0. Quitter");
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    menuInscription();
                    break;
                case 2:
                    menuValidation();
                    break;
                case 3:
                    menuConnexion();
                    break;
                case 4:
                    menuRechercheReservation();
                    break;
                case 5:
                    menuGestionReservations();
                    break;
                case 6:
                    menuAdministration();
                    break;
                case 0:
                    quitter = true;
                    System.out.println("Ciao Mostapha ! À bientôt !");
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }
    
    /**
     * Menu d'inscription
     */
    private void menuInscription() {
        System.out.println("\n Inscription ");
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        
        Utilisateur utilisateur = authentificationService.inscrire(email, motDePasse);
        
        if (utilisateur != null) {
            System.out.println("Inscription réussie token de validation envoyé ");
        } else {
            System.out.println("l'email est déjà utilisé.");
        }
    }
    
    /**
     * Menu de validation d'inscription
     */
    private void menuValidation() {
        System.out.println("\n Validation de l'inscription ");
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Code de validation: ");
        String code = scanner.nextLine();
        
        boolean resultat = authentificationService.validerCompte(email, code);
        
        if (resultat) {
            System.out.println("Compte validé");
        } else {
            System.out.println("email ou code de validation incorrect");
        }
    }
    
    /**
     * Menu de connexion
     */
    private void menuConnexion() {
        if (authentificationService.getUtilisateurConnecte() != null) {
            authentificationService.deconnecter();
            System.out.println("Vous avez été déconnecté.");
            return;
        }
        
        System.out.println("\n Connexion ");
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        
        Utilisateur utilisateur = authentificationService.connecter(email, motDePasse);
        
        if (utilisateur != null) {
            System.out.println("Connexion réussie! Bienvenue, " + utilisateur.getEmail());
        } else {
            System.out.println("Erreur: email ou mot de passe incorrect, ou compte non validé.");
        }
    }
    
    /**
     * Menu de recherche et réservation de borne
     */
    private void menuRechercheReservation() {
        Utilisateur utilisateur = authentificationService.getUtilisateurConnecte();
        if (utilisateur == null) {
            System.out.println("Vous devez être connecté pour accéder à cette fonctionnalité.");
            return;
        }
        
        System.out.println("\n Recherche & Réservation de borne ");
        
        LocalDateTime dateDebut = saisirDateTime("Date et heure de début (dd/MM/yyyy HH:mm): ");
        if (dateDebut == null) return;
        
        LocalDateTime dateFin = saisirDateTime("Date et heure de fin (dd/MM/yyyy HH:mm): ");
        if (dateFin == null) return;
        
        if (dateDebut.isAfter(dateFin)) {
            System.out.println("Erreur: la date de début doit être antérieure à la date de fin!");
            return;
        }
        
        // Recherche des bornes disponibles
        List<BorneRecharge> bornesDisponibles = reservationService.rechercherBornesDisponibles(dateDebut, dateFin);
        
        if (bornesDisponibles.isEmpty()) {
            System.out.println("Aucune borne disponible pour ce créneau.");
            return;
        }
        
        // Affichage des bornes disponibles
        System.out.println("\nBornes disponibles:");
        for (int i = 0; i < bornesDisponibles.size(); i++) {
            BorneRecharge borne = bornesDisponibles.get(i);
            System.out.println((i + 1) + ". " + borne);
        }
        
        // Choix de la borne
        System.out.print("\nChoisissez une borne (0 pour annuler): ");
        int choixBorne = lireEntier();
        
        if (choixBorne == 0 || choixBorne > bornesDisponibles.size()) {
            System.out.println("Opération annulée.");
            return;
        }
        
        BorneRecharge borneChoisie = bornesDisponibles.get(choixBorne - 1);
        
        // Création de la réservation
        Reservation reservation = reservationService.creerReservation(
            utilisateur.getId(), borneChoisie.getId(), dateDebut, dateFin);
        
        if (reservation != null) {
            System.out.println("Réservation créée avec succès! Statut: " + reservation.getStatut());
            System.out.println("Un opérateur examinera votre demande rapidement.");
        } else {
            System.out.println("Erreur lors de la création de la réservation.");
        }
    }
    
    /**
     * Menu de gestion des réservations
     */
    private void menuGestionReservations() {
        // Vérifier si l'utilisateur est connecté
        Utilisateur utilisateur = authentificationService.getUtilisateurConnecte();
        if (utilisateur == null) {
            System.out.println("Vous devez être connecté pour accéder à cette fonctionnalité.");
            return;
        }
        
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\nGestion des réservations ");
            System.out.println("1. Mes réservations");
            System.out.println("2. Gérer les réservations (mode opérateur)");
            System.out.println("0. Retour");
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    afficherMesReservations(utilisateur);
                    break;
                case 2:
                    gererReservationsOperateur();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }
    
    /**
     * Affiche les réservations de l'utilisateur connecté
     */
    private void afficherMesReservations(Utilisateur utilisateur) {
        List<Reservation> reservations = reservationService.getReservationsUtilisateur(utilisateur.getId());
        
        if (reservations.isEmpty()) {
            System.out.println("Vous n'avez aucune réservation.");
            return;
        }
        
        System.out.println("\nVos réservations:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
    
    /**
     * Mode opérateur pour gérer les réservations
     */
    private void gererReservationsOperateur() {
        System.out.println("\nMode Opérateur - Gestion des réservations ");
        
        List<Reservation> reservations = reservationService.getAllReservations();
        
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation disponible.");
            return;
        }
        
        System.out.println("\nRéservations en attente:");
        int compteur = 0;
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            if (r.getStatut() == StatutReservation.EN_ATTENTE) {
                System.out.println((i + 1) + ". " + r);
                compteur++;
            }
        }
        
        if (compteur == 0) {
            System.out.println("Aucune réservation en attente.");
            return;
        }
        
        System.out.print("\nChoisissez une réservation à traiter (0 pour annuler): ");
        int choix = lireEntier();
        
        if (choix == 0 || choix > reservations.size()) {
            System.out.println("Opération annulée.");
            return;
        }
        
        Reservation reservation = reservations.get(choix - 1);
        
        System.out.println("\nQue souhaitez-vous faire?");
        System.out.println("1. Accepter la réservation");
        System.out.println("2. Refuser la réservation");
        System.out.println("0. Annuler");
        System.out.print("Votre choix: ");
        
        int action = lireEntier();
        
        switch (action) {
            case 1:
                if (reservationService.changerStatutReservation(reservation.getId(), StatutReservation.ACCEPTEE)) {
                    System.out.println("Réservation acceptée!");
                    if (documentService.genererRecu(reservation)) {
                        System.out.println("Un reçu a été généré dans le dossier exports/");
                    } else {
                        System.out.println("Erreur lors de la génération du reçu.");
                    }
                } else {
                    System.out.println("Erreur lors du changement de statut.");
                }
                break;
            case 2:
                if (reservationService.changerStatutReservation(reservation.getId(), StatutReservation.REFUSEE)) {
                    System.out.println("Réservation refusée!");
                } else {
                    System.out.println("Erreur lors du changement de statut.");
                }
                break;
            case 0:
                System.out.println("Opération annulée.");
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    /**
     * Menu d'administration (lieux et bornes)
     */
    private void menuAdministration() {
        if (authentificationService.getUtilisateurConnecte() == null) {
            System.out.println("Vous devez être connecté pour accéder à cette fonctionnalité.");
            return;
        }
        
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\n Administration ");
            System.out.println("1. Voir tous les lieux");
            System.out.println("2. Ajouter un lieu");
            System.out.println("3. Modifier un lieu");
            System.out.println("4. Ajouter une borne");
            System.out.println("5. Modifier le tarif d'une borne");
            System.out.println("6. Supprimer une borne");
            System.out.println("0. Retour");
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    afficherTousLesLieux();
                    break;
                case 2:
                    ajouterLieu();
                    break;
                case 3:
                    modifierLieu();
                    break;
                case 4:
                    ajouterBorne();
                    break;
                case 5:
                    modifierTarifBorne();
                    break;
                case 6:
                    supprimerBorne();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }
    
    /**
     * Affiche tous les lieux et leurs bornes
     */
    private void afficherTousLesLieux() {
        List<LieuRecharge> lieux = borneService.getAllLieux();
        
        if (lieux.isEmpty()) {
            System.out.println("Aucun lieu disponible.");
            return;
        }
        
        System.out.println("\nListe des lieux:");
        for (LieuRecharge lieu : lieux) {
            System.out.println(lieu);
            
            if (lieu.getBornes().isEmpty()) {
                System.out.println("  Aucune borne dans ce lieu.");
            } else {
                for (BorneRecharge borne : lieu.getBornes()) {
                    System.out.println("  - " + borne);
                }
            }
        }
    }
    
    /**
     * Menu d'ajout d'un lieu
     */
    private void ajouterLieu() {
        System.out.println("\nAjout d'un lieu ");
        
        System.out.print("Nom du lieu: ");
        String nom = scanner.nextLine();
        
        System.out.print("Adresse: ");
        String adresse = scanner.nextLine();
        
        LieuRecharge lieu = borneService.ajouterLieu(nom, adresse);
        
        if (lieu != null) {
            System.out.println("Lieu ajouté avec succès: " + lieu);
        } else {
            System.out.println("Erreur lors de l'ajout du lieu.");
        }
    }
    
    /**
     * Menu de modification d'un lieu
     */
    private void modifierLieu() {
        System.out.println("\n Modification d'un lieu ");
        
        List<LieuRecharge> lieux = borneService.getAllLieux();
        
        if (lieux.isEmpty()) {
            System.out.println("Aucun lieu disponible.");
            return;
        }
        
        for (int i = 0; i < lieux.size(); i++) {
            System.out.println((i + 1) + ". " + lieux.get(i));
        }
        
        System.out.print("\nChoisissez un lieu à modifier (0 pour annuler): ");
        int choix = lireEntier();
        
        if (choix == 0 || choix > lieux.size()) {
            System.out.println("Opération annulée.");
            return;
        }
        
        LieuRecharge lieu = lieux.get(choix - 1);
        
        System.out.print("Nouveau nom (" + lieu.getNom() + "): ");
        String nom = scanner.nextLine();
        if (nom.isEmpty()) nom = lieu.getNom();
        
        System.out.print("Nouvelle adresse (" + lieu.getAdresse() + "): ");
        String adresse = scanner.nextLine();
        if (adresse.isEmpty()) adresse = lieu.getAdresse();
        
        if (borneService.modifierLieu(lieu.getId(), nom, adresse)) {
            System.out.println("Lieu modifié avec succès!");
        } else {
            System.out.println("Erreur lors de la modification du lieu.");
        }
    }
    
    /**
     * Menu d'ajout d'une borne
     */
    private void ajouterBorne() {
        System.out.println("\n Ajout d'une borne ");
        
        List<LieuRecharge> lieux = borneService.getAllLieux();
        
        if (lieux.isEmpty()) {
            System.out.println("Aucun lieu disponible. Veuillez d'abord créer un lieu.");
            return;
        }
        
        for (int i = 0; i < lieux.size(); i++) {
            System.out.println((i + 1) + ". " + lieux.get(i));
        }
        
        System.out.print("\nChoisissez un lieu pour la nouvelle borne (0 pour annuler): ");
        int choix = lireEntier();
        
        if (choix == 0 || choix > lieux.size()) {
            System.out.println("Opération annulée.");
            return;
        }
        
        LieuRecharge lieu = lieux.get(choix - 1);
        
        System.out.print("Tarif horaire (€/h): ");
        double tarif = lireDouble();
        
        if (tarif <= 0) {
            System.out.println("Le tarif doit être supérieur à 0.");
            return;
        }
        
        BorneRecharge borne = borneService.ajouterBorne(lieu.getId(), tarif);
        
        if (borne != null) {
            System.out.println("Borne ajoutée avec succès: " + borne);
        } else {
            System.out.println("Erreur lors de l'ajout de la borne.");
        }
    }
    
    /**
     * Menu de modification du tarif d'une borne
     */
    private void modifierTarifBorne() {
        System.out.println("\n Modification du tarif d'une borne ");
        
        afficherTousLesLieux();
        
        System.out.print("\nID de la borne à modifier (0 pour annuler): ");
        int borneId = lireEntier();
        
        if (borneId == 0) {
            System.out.println("Opération annulée.");
            return;
        }
        
        BorneRecharge borne = borneService.getBorneById(borneId);
        
        if (borne == null) {
            System.out.println("Borne non trouvée.");
            return;
        }
        
        System.out.print("Nouveau tarif horaire (" + borne.getTarifHoraire() + " €/h): ");
        double nouveauTarif = lireDouble();
        
        if (nouveauTarif <= 0) {
            System.out.println("Le tarif doit être supérieur à 0.");
            return;
        }
        
        if (borneService.modifierTarifBorne(borneId, nouveauTarif)) {
            System.out.println("Tarif modifié avec succès!");
        } else {
            System.out.println("Erreur lors de la modification du tarif.");
        }
    }
    
    /**
     * Menu de suppression d'une borne
     */
    private void supprimerBorne() {
        System.out.println("\n Suppression d'une borne ");
        
        afficherTousLesLieux();
        
        System.out.print("\nID de la borne à supprimer (0 pour annuler): ");
        int borneId = lireEntier();
        
        if (borneId == 0) {
            System.out.println("Opération annulée.");
            return;
        }
        
        BorneRecharge borne = borneService.getBorneById(borneId);
        
        if (borne == null) {
            System.out.println("Borne non trouvée.");
            return;
        }
        
        if (reservationService.borneAReservationsFutures(borneId)) {
            System.out.println("Impossible de supprimer cette borne: elle a des réservations futures.");
            return;
        }
        
        if (borneService.supprimerBorne(borneId)) {
            System.out.println("Borne supprimée avec succès!");
        } else {
            System.out.println("Erreur lors de la suppression de la borne.");
        }
    }
    
    /**
     * Méthode utilitaire pour lire un entier depuis la console
     */
    private int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0; 
        }
    }
    
    /**
     * Méthode utilitaire pour lire un double depuis la console
     */
    private double lireDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0; // Valeur par défaut en cas d'erreur
        }
    }
    
    /**
     * Méthode utilitaire pour saisir une date et heure
     */
    private LocalDateTime saisirDateTime(String message) {
        System.out.print(message);
        String input = scanner.nextLine();
        
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide. Utilisez le format dd/MM/yyyy HH:mm");
            return null;
        }
    }
} 