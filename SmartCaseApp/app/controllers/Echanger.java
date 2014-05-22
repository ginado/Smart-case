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

/**
 *
 * @author bombrunt
 */
public class Echanger extends Controller {
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
            if(Casier.allAreEmpty(casiers)) {
                return ok(views.html.accueil.render(utilisateur,"Tout les casiers sont vides, impossible d'échanger un objet"));
            } else {
                return ok(views.html.choix_casier.render(casiers,"Choisissez le casier avec lequel vous voulez echanger vorte objet.","Échanger","echanger"));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
    }
    
    static public Result echanger(String idCasier) {
        Casier casier ;
        Utilisateur utilisateur;
        try {
            casier = CasierDao.getCasier(idCasier);
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
        return ok(views.html.echanger_retirer.render(utilisateur,casier,true));
    }
    
    static public Result echangerFin(String idCasier) {
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        int id = Integer.parseInt(idCasier);
        try {
            TransactionDao.ajouterTransaction(new Transaction(0, date,"echange",SessionManager.get("utilisateur"),id));
            CasierDao.remplirCasier(id,200);
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
        return redirect("/main");
    }
}
