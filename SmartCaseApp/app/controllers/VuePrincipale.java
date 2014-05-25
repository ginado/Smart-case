package controllers;

import dao.UtilisateurDAO;
import java.sql.SQLException;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import utils.SessionManager;

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
            return(ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main")));
        }
        return ok(views.html.accueil.render(utilisateur,""));
    }
    
}
