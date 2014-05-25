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
import static play.mvc.Results.ok;

/**
 *
 * @author bombrunt
 */
public class Echanger extends ControllerCommandeArduino {
    static public Result choisir() {
        Collection<Casier> casiers;
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
            if(debugVerrou) {
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
        int id = Integer.parseInt(idCasier);
        int poids = -1;
        try {
            if (!debugSenseur) {
                poids = SerialClass.peserCasier(id);
                if (poids < seuil) {
                    redirect("/echangerfin/"+idCasier);
                }
            }
            if(!debugVerrou) {
                        SerialClass.fermerCasier(id);
                }
            
        } catch (Exception exception) {
            return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
        }
        
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Utilisateur utilisateur;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            System.out.println(utilisateur);
            Transaction transaction = new Transaction(0, date,"echange",utilisateur.getAdresseMail(),id);
            System.out.println(transaction);
            TransactionDao.ajouterTransaction(transaction);
            System.out.println(id+"!"+poids);
            CasierDao.remplirCasier(id,poids);
            return ok(views.html.accueil.render(utilisateur,"Votre échange a bien été pris en compte."));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }
    }
}
