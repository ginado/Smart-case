package controllers;

import utils.SessionManager;
import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Casier;
import models.Transaction;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 *
 * @author bombrunt
 */
public class Retirer extends Controller {
    static public Result choisir() {
Collection<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
        Utilisateur utilisateur =null;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            if(utilisateur==null || utilisateur.getCredit()<=0) {
                return ok(views.html.accueil.render(utilisateur,"Vous n'avez pas assez de crédit. Déposez des objets pour augmenter cotre solde de points."));
            }
            if(Casier.allAreEmpty(casiers)) {
                return ok(views.html.accueil.render(utilisateur,"Tout les casiers sont vides, impossible de retirer un objet"));
            } else {
                return ok(views.html.choix_casier.render(casiers,"Choisissez le casier dont vous voulez récuperer le contenu","Récuperer","retirer"));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }        
    }
    
   static public Result retirer(String idCasier) {
        Casier casier ;
        Utilisateur utilisateur;
        try {
            casier = CasierDao.getCasier(idCasier);
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
        return ok(views.html.echanger_retirer.render(utilisateur,casier,false));
    }
   
   static public Result retirerFin(String idCasier) {
       java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
       int id = Integer.parseInt(idCasier);
        try {
            TransactionDao.ajouterTransaction(new Transaction(0, date,"retrait",SessionManager.get("utilisateur"),id));
            CasierDao.viderCasier(id);
            UtilisateurDAO.ajouterCredit(SessionManager.get("utilisateur"),-1);
        } catch (SQLException ex) {
            Logger.getLogger(Retirer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redirect("/main");
    }
}
