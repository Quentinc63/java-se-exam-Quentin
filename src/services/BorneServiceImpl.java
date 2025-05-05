package services;

import interfaces.BorneService;
import java.util.ArrayList;
import java.util.List;
import model.BorneRecharge;
import model.LieuRecharge;

/**
 * Implémentation du service de gestion des bornes et lieux
 */
public class BorneServiceImpl implements BorneService {
    
    private List<LieuRecharge> lieux;
    private int nextLieuId;
    private int nextBorneId;
    
    public BorneServiceImpl() {
        this.lieux = new ArrayList<>();
        this.nextLieuId = 1;
        this.nextBorneId = 1;
        initDonneesTest();
    }
    
    private void initDonneesTest() {
        LieuRecharge lieu = ajouterLieu("Human booster", "222 Gustave flobert Clermont-Ferrand");
        ajouterBorne(lieu.getId(), 5.5);  // 5.5€/h
        ajouterBorne(lieu.getId(), 7.0);  // 7.0€/h
        LieuRecharge lieu2 = ajouterLieu("Chez moi", "29 rue du nord Coudes");
        ajouterBorne(lieu2.getId(), 6.0);
        ajouterBorne(lieu2.getId(), 6.5);
    }
    
    @Override
    public LieuRecharge ajouterLieu(String nom, String adresse) {
        LieuRecharge nouveauLieu = new LieuRecharge(nextLieuId++, nom, adresse);
        lieux.add(nouveauLieu);
        return nouveauLieu;
    }
    
    @Override
    public boolean modifierLieu(int lieuId, String nom, String adresse) {
        LieuRecharge lieu = getLieuById(lieuId);
        if (lieu == null) {
            return false;
        }
        
        lieu.setNom(nom);
        lieu.setAdresse(adresse);
        return true;
    }
    
    @Override
    public List<LieuRecharge> getAllLieux() {
        return new ArrayList<>(lieux);
    }
    
    @Override
    public LieuRecharge getLieuById(int id) {
        for (LieuRecharge lieu : lieux) {
            if (lieu.getId() == id) {
                return lieu;
            }
        }
        return null;
    }
    
    @Override
    public BorneRecharge ajouterBorne(int lieuId, double tarifHoraire) {
        LieuRecharge lieu = getLieuById(lieuId);
        if (lieu == null) {
            return null;
        }
        
        BorneRecharge nouvelleBorne = new BorneRecharge(nextBorneId++, tarifHoraire);
        lieu.ajouterBorne(nouvelleBorne);
        return nouvelleBorne;
    }
    
    @Override
    public boolean modifierTarifBorne(int borneId, double nouveauTarif) {
        BorneRecharge borne = getBorneById(borneId);
        if (borne == null) {
            return false;
        }
        
        borne.setTarifHoraire(nouveauTarif);
        return true;
    }
    
    @Override
    public boolean supprimerBorne(int borneId) {
        for (LieuRecharge lieu : lieux) {
            for (BorneRecharge borne : lieu.getBornes()) {
                if (borne.getId() == borneId) {
                    lieu.supprimerBorne(borne);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public BorneRecharge getBorneById(int id) {
        for (LieuRecharge lieu : lieux) {
            for (BorneRecharge borne : lieu.getBornes()) {
                if (borne.getId() == id) {
                    return borne;
                }
            }
        }
        return null;
    }
} 