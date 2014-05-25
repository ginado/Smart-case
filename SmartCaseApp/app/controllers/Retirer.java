package controllers;

import arduino.SerialClass;
import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Casier;
import models.Transaction;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import utils.SessionManager;

/**
 *
 * @author bombrunt
 */
public class Retirer extends ControllerCommandeArduino {
    static public Result choisir() {
List<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }
        try {
            Utilisateur utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            if(utilisateur==null || utilisateur.getCredit()<=0) {
                return ok(views.html.accueil.render(utilisateur,"Vous n'avez pas assez de crédit. Déposez des objets pour augmenter cotre solde de points."));
            }
            if(Casier.allAreEmpty(casiers)) {
                return ok(views.html.accueil.render(utilisateur,"Tout les casiers sont vides, impossible de retirer un objet"));
            } else {
                return ok(views.html.choix_casier.render(casiers,"Choisissez le casier dont vous voulez récuperer le contenu","Récuperer","retirer"));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }        
    }
    
   static public Result retirer(String idCasier) {
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
        return ok(views.html.echanger_retirer.render(utilisateur,casier,false));
    }
   
   static public Result retirerFin(String idCasier) {
       java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
       Integer id = Integer.parseInt(idCasier);
        try {
            if(!debugVerrou) {
                try {
                    SerialClass.fermerCasier(id);
                } catch (Exception ex) {
                    return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
                }
            }
            Utilisateur utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            TransactionDao.ajouterTransaction(new Transaction(0, date,"retrait",utilisateur.getAdresseMail(),id));
            CasierDao.viderCasier(id);
            UtilisateurDAO.ajouterCredit(utilisateur.getAdresseMail(),-1);
            utilisateur.ajouterCredit(-1);
            return ok(views.html.accueil.render(utilisateur,"Votre retrait a bien été pris en compte."));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne : "+ex.getMessage(),"/main"));
        }
    }
}
