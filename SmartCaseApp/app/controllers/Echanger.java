package controllers;

import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import models.Casier;
import models.Transaction;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import utils.SessionManager;
import arduino.*;
import java.util.List;
import static play.mvc.Results.ok;

/**
 *
 * @author bombrunt
 */
public class Echanger extends ControllerCommandeArduino {
    static public Result choisir() {
        List<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }
        Utilisateur utilisateur =null;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            if(Casier.allAreEmpty(casiers)) {
                return ok(views.html.accueil.render(utilisateur,"Tout les casiers sont vides, impossible d'échanger un objet"));
            } else {
                return ok(views.html.choix_casier.render(casiers,"Choisissez le casier avec lequel vous voulez echanger vorte objet.","Échanger","echanger"));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }
    }
    
    static public Result echanger(String idCasier) {
        Casier casier ;
        Utilisateur utilisateur;
        try {
            casier = CasierDao.getCasier(idCasier);
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            if(!debugVerrou) {
                try {
                    SerialClass.ouvrirCasier(casier.getIdCasier());
                } catch (Exception ex) {
                    return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
                }
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }    
	return ok(views.html.echanger_retirer.render(utilisateur,casier,true));
    }
    
    static public Result echangerFin(String idCasier) {
        Integer id = Integer.parseInt(idCasier);
        Integer poids = -1;
        try {
        Utilisateur utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        Casier casier = CasierDao.getCasier(idCasier);


            if (!debugSenseur) {
                poids = SerialClass.peserCasier(id);
                if (poids < seuil) {
                    return ok(views.html.echanger_retirer.render(utilisateur,casier,true));
                }
            }
            if(!debugVerrou) {
                        SerialClass.fermerCasier(id);
                }

        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
            Transaction transaction = new Transaction(0, date,"echange",utilisateur.getAdresseMail(),id);
            TransactionDao.ajouterTransaction(transaction);
            CasierDao.remplirCasier(id,poids);
            return ok(views.html.accueil.render(utilisateur,"Votre échange a bien été pris en compte. Pensez a fermer la porte !"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        } catch (Exception exception) {
            return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
        }
    }
}
