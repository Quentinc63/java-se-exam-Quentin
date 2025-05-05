import services.AuthentificationServiceImpl;
import services.BorneServiceImpl;
import services.DocumentServiceImpl;
import services.ReservationServiceImpl;
import ui.Menu;

/**
 * Classe principale de l'application Electricity Business
 */
public class App {
    public static void main(String[] args) {

        AuthentificationServiceImpl authentificationService = new AuthentificationServiceImpl();
        BorneServiceImpl borneService = new BorneServiceImpl();
        ReservationServiceImpl reservationService = new ReservationServiceImpl(authentificationService, borneService);
        DocumentServiceImpl documentService = new DocumentServiceImpl(reservationService);
        
        // Démarrage de l'application
        Menu menu = new Menu(authentificationService, borneService, reservationService, documentService);
        menu.afficherMenuPrincipal();
    }
}
