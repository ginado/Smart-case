package controllers;

import dao.CasierDao;
import dao.UtilisateurDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Casier;
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
            return ok(views.html.error.render("Erreur interne."));
        }
        Utilisateur utilisateur =null;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        if(utilisateur==null || utilisateur.getCredit()<=0) {
            return ok(views.html.error.render("Vous n'avez pas assez de crédit. Déposez des objets pour augmenter cotre solde de points."));
        }
        return ok(views.html.echanger_retirer_choix.render(utilisateur,casiers,false));
    }
    
   static public Result retirer(String idCasier) {
        Casier casier ;
        Utilisateur utilisateur;
        try {
            casier = CasierDao.getCasier(idCasier);
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        return ok(views.html.echanger_retirer.render(utilisateur,casier,false));
    }
   
   static public Result retirerFin() {
        try {
            UtilisateurDAO.ajouterCredit(SessionManager.get("utilisateur"),-1);
        } catch (SQLException ex) {
            Logger.getLogger(Retirer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redirect("/main");
    }
}
