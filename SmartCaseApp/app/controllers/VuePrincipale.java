package controllers;

import utils.SessionManager;
import dao.UtilisateurDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;

/**
 *
 * @author bombrunt
 */
public class VuePrincipale extends Controller {
    public static Result main(){
        Utilisateur utilisateur = null;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
        } catch (SQLException ex) {
            return(ok(views.html.error.render("Erreur interne.")));
        }
        return ok(views.html.accueil.render(utilisateur,""));
    }
    
}
