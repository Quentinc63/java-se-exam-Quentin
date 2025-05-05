package services;

import interfaces.DocumentService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Reservation;
import model.StatutReservation;

/**
 * Implémentation du service de génération de documents
 */
public class DocumentServiceImpl implements DocumentService {
    
    private static final String EXPORTS_DIR = "exports";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private ReservationServiceImpl reservationService;
    
    public DocumentServiceImpl(ReservationServiceImpl reservationService) {
        this.reservationService = reservationService;
        File exportsDir = new File(EXPORTS_DIR);
        if (!exportsDir.exists()) {
            exportsDir.mkdir();
        }
    }
    
    @Override
    public boolean genererRecu(Reservation reservation) {
        if (reservation == null || reservation.getStatut() != StatutReservation.ACCEPTEE) {
            return false;
        }
        
        String nomFichier = EXPORTS_DIR + File.separator + "recu_" + reservation.getId() + ".txt";
        
        try (FileWriter writer = new FileWriter(nomFichier)) {
            writer.write("Réservation #" + reservation.getId() + "\n\n");
            writer.write("Client: " + reservation.getUtilisateur().getEmail() + "\n");
            writer.write("Date d'achat: " + java.time.LocalDateTime.now().format(FORMATTER) + "\n\n");
            
            writer.write("Détails de la réservation:\n");
            writer.write("Borne #" + reservation.getBorne().getId() + "\n");
            writer.write("Tarif horaire: " + reservation.getBorne().getTarifHoraire() + "€/h\n");
            writer.write("Du: " + reservation.getDateDebut().format(FORMATTER) + "\n");
            writer.write("Au: " + reservation.getDateFin().format(FORMATTER) + "\n\n");

            double prix = reservation.calculerPrixTotal();
            writer.write("Montant total: " + String.format("%.2f", prix) + "€\n\n");
            
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du reçu: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean exporterHistorique() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return false;
        }
        
        String nomFichier = EXPORTS_DIR + File.separator + "historique_" + 
                java.time.LocalDate.now() + ".txt";
        
        try (FileWriter writer = new FileWriter(nomFichier)) {
            writer.write("ID;Client;Borne;DateDebut;DateFin;Statut;Prix\n");
            
            for (Reservation reservation : reservations) {
                writer.write(
                    reservation.getId() + ";" + 
                    reservation.getUtilisateur().getEmail() + ";" + 
                    reservation.getBorne().getId() + ";" + 
                    reservation.getDateDebut().format(FORMATTER) + ";" + 
                    reservation.getDateFin().format(FORMATTER) + ";" + 
                    reservation.getStatut() + ";" + 
                    String.format("%.2f", reservation.calculerPrixTotal()) + "\n"
                );
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export de l'historique: " + e.getMessage());
            return false;
        }
    }
} 