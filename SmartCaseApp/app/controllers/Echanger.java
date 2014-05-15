package controllers;

import dao.CasierDao;
import dao.UtilisateurDAO;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Casier;
import models.Utilisateur;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;

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
            return ok(views.html.error.render("Erreur interne."));
        }
        Utilisateur utilisateur =null;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        return ok(views.html.echanger_retirer_choix.render(utilisateur,casiers,true));
    }
    
    static public Result echanger(String idCasier) {
        Casier casier ;
        Utilisateur utilisateur;
        try {
            casier = CasierDao.getCasier(idCasier);
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        return ok(views.html.echanger_retirer.render(utilisateur,casier,true));
    }
    
    static public Result echangerFin() {
        return redirect("/main");
    }
}
